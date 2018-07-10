package cn.seu.edu.LANComm.ui;

import cn.seu.edu.LANComm.communication.EthernetPacketSender;
import cn.seu.edu.LANComm.communication.dispatcher.ConstellationDataPacketDispatcher;
import cn.seu.edu.LANComm.communication.dispatcher.HoppingPatternDataPacketDispatcher;
import cn.seu.edu.LANComm.communication.dispatcher.IntermediateFrequencyDataPacketDispatcher;
import cn.seu.edu.LANComm.communication.dispatcher.ReceivedSymbolPacketDispatcher;
import cn.seu.edu.LANComm.communication.dispatcher.TransmittedSymbolPacketDispatcher;
import cn.seu.edu.LANComm.communication.receiver.DataReceiver;
import cn.seu.edu.LANComm.communication.util.DataLinkParameterEnum;
import cn.seu.edu.LANComm.communication.util.FramingDecoder;
import cn.seu.edu.LANComm.communication.util.MACStringConvertor;
import cn.seu.edu.LANComm.communication.util.NetworkInterfaceUtil;
import cn.seu.edu.LANComm.util.DumpFileUtil;
import jpcap.JpcapCaptor;
import jpcap.JpcapWriter;
import jpcap.NetworkInterface;
import jpcap.PacketReceiver;
import jpcap.packet.EthernetPacket;
import jpcap.packet.Packet;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Administrator on 2018/1/26.
 * @author WYCPhoenix
 * @date 2018-1-29-15:46
 */
public class MainFrame {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                FrameSet frameSet = new FrameSet("实时数据显示");
                frameSet.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frameSet.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        frameSet.dispose();
                        // 等待2s，程序退出
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }finally {
                            System.exit(0);
                        }

                    }
                });
                frameSet.setVisible(true);
            }
        });
    }
}

class FrameSet extends JFrame {

    UIParameterCollector collector = new UIParameterCollector();
    private JButton confirmButton;

    /**
     * 四个控制绘图的线程
     */
    private PlotIntermediateFrequencyPart plotIntermediateFrequencyPart;
    private PlotIntermediateFrequencyFFTPart plotIntermediateFrequencyFFTPart;
    private PlotConstellationDiagramPart plotConstellationDiagramPart;
    private PlotHoppingPatternPart plotHoppingPatternPart;

    /**
     *
     */
    private CommunicationStatusPart communicationStatusPart;
    /**
     * 5个控制数据接收的线程
     */
    private ConstellationDataPacketDispatcher constellationDataPacketDispatcher;
    private HoppingPatternDataPacketDispatcher hoppingPatternDataPacketDispatcher;
    private IntermediateFrequencyDataPacketDispatcher intermediateFrequencyDataPacketDispatcher;
    private ReceivedSymbolPacketDispatcher receivedSymbolPacketDispatcher;
    private TransmittedSymbolPacketDispatcher transmittedSymbolPacketDispatcher;

    /**
     * dumpfile写入
     */
    JpcapWriter intermediateFrequencyDataWriter;

    /**
     * 接收的星座数据
     */
    BlockingQueue<Packet> constellationData = new LinkedBlockingQueue<>();
    /**
     * 接收的中频信号
     */
    BlockingQueue<Packet> intermediateFrequenceData = new LinkedBlockingQueue<>();
    /**
     * 接收的中频信号，用于计算FFT
     */
    BlockingQueue<Packet> intermediateFrequenceDataFFT = new LinkedBlockingQueue<>();
    /**
     * 跳频图案
     */
    BlockingQueue<Packet> hoppingPatternData = new LinkedBlockingQueue<>();
    /**
     * 发送的符号
     */
    BlockingQueue<Packet> transmittedSymbol = new LinkedBlockingQueue<>();
    /**
     * 接收的符号
     */
    BlockingQueue<Packet> receivedSymbol = new LinkedBlockingQueue<>();

    private Map<String, Float> sampleRate = new HashMap<>();

    /**
     *  线程控制标识，在发送和结束之间切换
     */
    private volatile boolean sendStarted = false;

    private volatile boolean initThreadFlag = false;

    /**
     * 设置最优尺寸
     */
    private static final int DEFAULT_WIDTH = 1000;
    private static final int DEFAULT_HEIGHT = 300;
    /**
     * 下位机停止指令
     */
    private static final String STOP_COMMAND = "停止";
    /**
     * 配置参数据发送
     */
    private static final String SEND_DATA = "发送";
    /**
     * 接收数据
     */
    private static final String RECEIVE_DATA = "接收";
    /**
     * 工作状态确认
     */
    private static final String CONFIRM = "确认";

    /**
     * 跳频模式特征，mode以-FH结尾
     */
    private static final String FH = "-FH";
    /**
     * dump文件位置的key
     */
    private static final String INTERMEDIATEFREQUENCY_DATA = "IntermediateFrequencyData.log";
    private static final String CONSTELLATION_DATA = "ConstellationData.log";
    private static final String HOPPING_PATTERN_DATA = "HoppingPatternData.log";
    private static final String TRANSMITTED_SYMBOL = "TransmittedSymbol.log";
    private static final String RECEIVED_SYMBOL = "ReceivedData.log";


    public FrameSet(String title) {
        // 主窗口大小
        super.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        // 主窗口居中
        super.setLocationRelativeTo(null);
        // 主窗口标题
        super.setTitle(title);
        // 主窗口大小可调
        super.setResizable(false);
        // ICON
        String path = System.getProperty("user.dir").replace('\\', '/') + "/config/";
        Image mainFrameIcon = new ImageIcon(path + "MainFrame.png").getImage();
        super.setIconImage(mainFrameIcon);
        super.setLayout(new FlowLayout());

        // MAC地址交换
        MACExchangeDialog.showDialog(collector);

        // 参数设置面板
        JPanel parameterPanel = new JPanel();
        parameterPanel.setBackground(Color.WHITE);
        // 通信模式选择部分
        parameterPanel.add(CommunicationModeSelectorAndParameterSettingPart.createCommunicationModeSelectorAndParameterSettingPanel("LANComm.properties", collector));

        // 通信状态部分
        Map<String, Object> statusPanel = CommunicationStatusPart.createStatusPanel();
        parameterPanel.add((JPanel)statusPanel.get(CommunicationStatusPart.getPanelKey()));

        // 通信收发确认部分
        Map<String, Object> txRxSelectorPart = CommunicationTXRxSelectorPart.createCommunicationTXRxSelectorPanel(collector);
        parameterPanel.add((JPanel)txRxSelectorPart.get(CommunicationTXRxSelectorPart.getStatusPanel()));
        confirmButton = (JButton) txRxSelectorPart.get(CommunicationTXRxSelectorPart.getConfirmButton());

        // 绘图面板
        JPanel plotPanel = new JPanel(new GridLayout(1,4));
        plotPanel.setBackground(Color.WHITE);
        plotPanel.setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
        // 中频时域图
        JPanel intermediateFrequencyPartPanel = new JPanel();
        intermediateFrequencyPartPanel.setBackground(Color.WHITE);
        intermediateFrequencyPartPanel.setSize(new Dimension(DEFAULT_WIDTH / 4, DEFAULT_HEIGHT));
        plotIntermediateFrequencyPart = new PlotIntermediateFrequencyPart(intermediateFrequencyPartPanel);
        plotIntermediateFrequencyPart.createIntermediateFrequencyChart(intermediateFrequenceData);
        plotPanel.add(intermediateFrequencyPartPanel);
        // 中频功率谱图
        JPanel intermediateFrequencyFFTPartPanel  = new JPanel();
        intermediateFrequencyFFTPartPanel.setBackground(Color.WHITE);
        intermediateFrequencyFFTPartPanel.setSize(new Dimension(DEFAULT_WIDTH / 4, DEFAULT_HEIGHT));
         plotIntermediateFrequencyFFTPart = new PlotIntermediateFrequencyFFTPart(intermediateFrequencyFFTPartPanel);
        plotIntermediateFrequencyFFTPart.createIntermediateFrequencyFFTChart(intermediateFrequenceDataFFT);
        plotPanel.add(intermediateFrequencyFFTPartPanel);
        // 星座图
        JPanel constellationDiagramPartPanel = new JPanel();
        constellationDiagramPartPanel.setBackground(Color.WHITE);
        constellationDiagramPartPanel.setSize(new Dimension(DEFAULT_WIDTH / 4, DEFAULT_HEIGHT));
        plotConstellationDiagramPart = new PlotConstellationDiagramPart(constellationDiagramPartPanel);
        plotConstellationDiagramPart.createConstellationDiagramChart(constellationData);
        plotPanel.add(constellationDiagramPartPanel);
        // 跳频图案图
        JPanel hoppingPatterPartPanel = new JPanel();
        hoppingPatterPartPanel.setBackground(Color.WHITE);
        hoppingPatterPartPanel.setSize(new Dimension(DEFAULT_WIDTH / 4, DEFAULT_HEIGHT));
        plotHoppingPatternPart = new PlotHoppingPatternPart(hoppingPatterPartPanel);
        plotHoppingPatternPart.createHoppingPatternChart(hoppingPatternData);
        plotPanel.add(hoppingPatterPartPanel);
        // 组件汇总
        JPanel panel = new JPanel(new GridLayout(2, 1));
        panel.setBackground(Color.WHITE);
        panel.add(parameterPanel);
        panel.add(plotPanel);
        super.add(panel);
        super.pack();

        /**
         * 这里会比较复杂，单独拿出来，控制线程的启停与数据的收发
         * 讲道理，这个actionListener不应该在这里，已经放着了就不动了
         */
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (collector.getSwitchTransmitAndReceive().equals(SEND_DATA)){
                    EthernetPacketSender.sendEthernetPacket(new String[]{collector.getTxMAC(), collector.getRxMAC()},
                            collector.getLocalMAC(), DataLinkParameterEnum.PARAMETER_SETTING, collector.getParameterSelected());
                    // 接收中频采样信号, 一次性使用，写一个匿名内部类算球~
                    // 有空再重构~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                    // 根据过滤器规则，MAC地址使用":"分隔而不是"-"
                    String srcFilter = "ether src " + collector.getRxMAC().replace("-", ":") + " or " + collector.getTxMAC().replace("-", ":");
                    String localMAC = collector.getLocalMAC();
                    try {
                        NetworkInterface deviceUsed = NetworkInterfaceUtil.getDesignateDeviceByMACString(localMAC);
                        if (deviceUsed != null) {
                            JpcapCaptor jpcapCaptor = JpcapCaptor.openDevice(deviceUsed, 2000, false, 2000);
                            jpcapCaptor.setFilter(srcFilter, true);
                            PacketReceiver sampleRateReceiver = new PacketReceiver() {
                                @Override
                                public void receivePacket(Packet packet) {
                                    EthernetPacket ethernetPacket = (EthernetPacket) packet.datalink;
                                    // 对帧的FrameType进行过滤
                                    if (Short.parseShort(DataLinkParameterEnum.FRAME_TYPE.getDataType()) == ethernetPacket.frametype) {
                                        FramingDecoder decoder = new FramingDecoder(packet.data);
                                        if (decoder.getParameterIDentifier().getDataType().equals(DataLinkParameterEnum.SAMPLE_RATE.getDataType())) {
                                            // 对源地址进行过滤
                                            if (MACStringConvertor.macToString(ethernetPacket.src_mac).equals(collector.getTxMAC())) {
                                                float[] sampleRateTx = decoder.getTransmittedData();
                                                sampleRate.put(collector.getTxMAC(), new Float(sampleRateTx[0]));
                                            } else if (MACStringConvertor.macToString(ethernetPacket.src_mac).equals(collector.getRxMAC())) {
                                                float[] sampleRateRx = decoder.getTransmittedData();
                                                sampleRate.put(collector.getRxMAC(), new Float(sampleRateRx[0]));
                                            }
                                            if (sampleRate.keySet().size() == 2) {
                                                // 设置中频采样率
                                                plotIntermediateFrequencyFFTPart.setSampleRate(sampleRate.get(collector.getRxMAC()));
                                                jpcapCaptor.breakLoop();
                                                TimedDialog.getDialog("消息","发送端TxMAC: " + collector.getTxMAC() + " 采样率: " + sampleRate.get(collector.getTxMAC()).floatValue() + "\n" +
                                                                                             "接收端RxMAC: " + collector.getRxMAC() + "采样率: " + sampleRate.get(collector.getRxMAC()).floatValue() + "\n"
                                                                                            + collector.toString(), JOptionPane.INFORMATION_MESSAGE, false,10000);
                                                if (sampleRate.get(collector.getTxMAC()).equals(sampleRate.get(collector.getRxMAC()))) {
                                                    sendStarted = true;
                                                } else {
                                                    TimedDialog.getDialog("错误","收发端采样率不一致！" +
                                                            "发送端采样率: " + sampleRate.get(collector.getRxMAC()).floatValue() +
                                                            "接收端采样率: " + sampleRate.get(collector.getRxMAC()).floatValue(),
                                                            JOptionPane.ERROR_MESSAGE, false,0);
                                                    sendStarted = false;
                                                }
                                            }
                                        }
                                    }
                                }
                            };
                            /**
                             * 增加一个定时器，解决无法接收下位中频采样率时软件阻塞问题
                             */
                            Timer timer = new Timer();
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    if (!sendStarted) {
                                        jpcapCaptor.breakLoop();
                                        TimedDialog.getDialog("超时", "接收下位机中频采样率超时，检查下位机状态或MAC地址", JOptionPane.ERROR_MESSAGE, true, 0);
                                    }
                                }
                            }, 5000);
                            jpcapCaptor.loopPacket(-1, sampleRateReceiver);
                            if (jpcapCaptor != null) {
                                jpcapCaptor.close();
                            }
                        }
                    } catch (IOException e1) {
                        TimedDialog.getDialog("错误","网卡打开失败，检查上位机MAC地址或者重启程序", JOptionPane.ERROR_MESSAGE, false,0);
                    }
                } else if (collector.getSwitchTransmitAndReceive().equals(RECEIVE_DATA)){
                    if (sendStarted) {
                        if (initThreadFlag) {
                            // 恢复被关闭的线程的状态控制位
                            plotIntermediateFrequencyPart.startThread();
                            plotIntermediateFrequencyFFTPart.startThread();
                            plotConstellationDiagramPart.startThread();
                            plotHoppingPatternPart.startThread();
                            communicationStatusPart.setRunning(true);
                            constellationDataPacketDispatcher.setRunning(true);
                            intermediateFrequencyDataPacketDispatcher.setRunning(true);
                            receivedSymbolPacketDispatcher.setRunning(true);
                            transmittedSymbolPacketDispatcher.setRunning(true);

                            intermediateFrequenceData.clear();
                            intermediateFrequenceDataFFT.clear();
                            hoppingPatternData.clear();
                            constellationData.clear();
                            transmittedSymbol.clear();
                            receivedSymbol.clear();
                        }
//                        // dump文件的位置
                        Map<String, String> pathMap = DumpFileUtil.getDumFilePath();
                        String intermediateFrequencyDataDumpFilePath = pathMap.get(INTERMEDIATEFREQUENCY_DATA);
                        /**
                         * 下面四种数据类型因不需要写入文件，暂时保留名字在这里
                         */
                        String constellationDataDumpFilePath = pathMap.get(CONSTELLATION_DATA);
                        String hoppingPatternDataDumpFilePath = pathMap.get(HOPPING_PATTERN_DATA);
                        String transmittedSymbolDataDumpFilePath = pathMap.get(TRANSMITTED_SYMBOL);
                        String receivedSymbolDataDumpFilePath = pathMap.get(RECEIVED_SYMBOL);

                        // 发送启动指示
                        EthernetPacketSender.sendEthernetPacket(new String[]{collector.getRxMAC(), collector.getTxMAC()}, collector.getLocalMAC(),
                                DataLinkParameterEnum.COMMUNICATION_START, new float[]{0F});

                        // 各种数据的接收线程
                        String RxFilter = "ether src " + collector.getRxMAC().replace("-", ":");
                        // 中频信号接收
                        Thread intermediateFrequenceDataPlotter = new Thread(plotIntermediateFrequencyPart);
                        intermediateFrequenceDataPlotter.start();
                        intermediateFrequencyDataPacketDispatcher = new IntermediateFrequencyDataPacketDispatcher(4000,
                                                                                            intermediateFrequenceData, intermediateFrequenceDataFFT);
                        DataReceiver intermediateFrequencyDataReceiver = new DataReceiver(collector.getLocalMAC(),
                                RxFilter, intermediateFrequencyDataPacketDispatcher);
                        new Thread(intermediateFrequencyDataReceiver).start();

                        // 中频信号的下写入
                        intermediateFrequencyDataPacketDispatcher.setCaptor(intermediateFrequencyDataReceiver.getCaptor());
                        try {
                            JpcapCaptor captor = intermediateFrequencyDataReceiver.getCaptor();
                            intermediateFrequencyDataWriter = JpcapWriter.openDumpFile(captor, intermediateFrequencyDataDumpFilePath);
                            intermediateFrequencyDataPacketDispatcher.setWriter(intermediateFrequencyDataWriter);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }

                        // 中频信号FFT接收
                        Thread intermediateFrequencyFFTPlotter = new Thread(plotIntermediateFrequencyFFTPart);
                        intermediateFrequencyFFTPlotter.start();
                        
                        // 接收端星座数据接收
                        Thread constellationDiagramPlotter = new Thread(plotConstellationDiagramPart);
                        constellationDiagramPlotter.start();
                        constellationDataPacketDispatcher = new ConstellationDataPacketDispatcher(4000, constellationData);
                        DataReceiver constellationDataReceiver = new DataReceiver(collector.getLocalMAC(),
                                RxFilter, constellationDataPacketDispatcher);
                        constellationDataPacketDispatcher.setCaptor(constellationDataReceiver.getCaptor());
                        new Thread(constellationDataReceiver).start();

                        // 星座数据的写入

                        // 只有在跳频模式下启动跳频图案接收
                        if (collector.getMode().endsWith(FH)) {
                            // 接收端跳频图案
                            Thread hoppingPatternPlotter = new Thread(plotHoppingPatternPart);
                            hoppingPatternPlotter.start();

                            hoppingPatternDataPacketDispatcher = new HoppingPatternDataPacketDispatcher(4000, hoppingPatternData);
                            hoppingPatternDataPacketDispatcher.setRunning(true);
                            DataReceiver hoppingPatternDataReceiver = new DataReceiver(collector.getLocalMAC(), RxFilter, hoppingPatternDataPacketDispatcher);
                            hoppingPatternDataPacketDispatcher.setCaptor(hoppingPatternDataReceiver.getCaptor());
                            new Thread(hoppingPatternDataReceiver).start();
                        }

                        // 误码率计算线程
                        String TxFilter = "ether src " + collector.getTxMAC().replace("-", ":");
                        transmittedSymbolPacketDispatcher = new TransmittedSymbolPacketDispatcher(4000, transmittedSymbol);
                        DataReceiver transmittedDataReceiver = new DataReceiver(collector.getLocalMAC(), TxFilter, transmittedSymbolPacketDispatcher);
                        transmittedSymbolPacketDispatcher.setCaptor(transmittedDataReceiver.getCaptor());
                        receivedSymbolPacketDispatcher = new ReceivedSymbolPacketDispatcher(4000, receivedSymbol);

                        DataReceiver receivedDataReceiver = new DataReceiver(collector.getLocalMAC(), RxFilter, receivedSymbolPacketDispatcher);
                        receivedSymbolPacketDispatcher.setCaptor(receivedDataReceiver.getCaptor());
                        new Thread(transmittedDataReceiver).start();
                        new Thread(receivedDataReceiver).start();
                        Thread symbolErrorRate = new Thread(communicationStatusPart = new CommunicationStatusPart(transmittedSymbol, receivedSymbol));
                        symbolErrorRate.start();

                        confirmButton.setActionCommand(STOP_COMMAND);
                        confirmButton.setText(STOP_COMMAND);
                        
                    } else {
                        JOptionPane.showMessageDialog(null, "先发送配置参数，在启动数据接收", "提示", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        });


        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (confirmButton.getActionCommand().equals(STOP_COMMAND)) {
                    sendStarted = false;
                    initThreadFlag = true;
                    confirmButton.setActionCommand(SEND_DATA);
                    confirmButton.setText(CONFIRM);
                    /**
                     * 停止绘图线程
                     */
                    plotIntermediateFrequencyPart.stopThread();
                    plotIntermediateFrequencyFFTPart.stopThread();
                    plotConstellationDiagramPart.stopThread();
                    plotHoppingPatternPart.stopThread();
                    /**
                     * 停止误码率计算线程
                     */
                    communicationStatusPart.setRunning(false);
                    // 这里等待 0.5 s,等待后台线程停止
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    /**
                     * 停止数据接收线程
                     */
                    constellationDataPacketDispatcher.setRunning(false);
                    if (collector.getMode().endsWith(FH)) {
                        hoppingPatternDataPacketDispatcher.setRunning(false);
                    }
                    intermediateFrequencyDataPacketDispatcher.setRunning(false);
                    receivedSymbolPacketDispatcher.setRunning(false);
                    transmittedSymbolPacketDispatcher.setRunning(false);
                    /**
                     * 发送停止指令，下位机回到接收参数状态
                     */
                    EthernetPacketSender.sendEthernetPacket(new String[]{collector.getTxMAC(), collector.getRxMAC()},
                            collector.getLocalMAC(), DataLinkParameterEnum.COMMUNICATION_STOP, new float[]{0.0F});
                    JOptionPane.showMessageDialog(null, "停止接收，请重新设置参数开始新一轮数据接收");
                }
            }
        });
    }
}
