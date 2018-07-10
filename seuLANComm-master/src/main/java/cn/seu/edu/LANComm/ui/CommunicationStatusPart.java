package cn.seu.edu.LANComm.ui;

import cn.seu.edu.LANComm.communication.util.FramingDecoder;
import cn.seu.edu.LANComm.util.FontEnum;
import jpcap.packet.Packet;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;


/**
 * Created by Administrator on 2018/1/27.
 * @author WYCPhoenix
 */
public class CommunicationStatusPart implements Runnable{
    /**
     * 每次计算误符号率使用的帧的个数
     */
    private static final int PACKAGE_PER_CAL = 10;
    /**
     * 两种工作状态常量
     */
    private static final String NORMAL = " 正 常 ";
    private static final String ABNORMAL = " 故 障 ";
    /**
     * 状态栏的默认大小
     */
    private static final int DEFAULT_WIDTH = 200;
    private static final int DEFAULT_HEIGHT = 300;
    /**
     * 主panel网格布局参数
     */
    private static final int DEFAULT_GRID_ROWS = 6;
    private static final int DEFAULT_GRID_COLUMN = 1;
    /**
     *
     */
    private static final String PANEL_KEY = "StatusPanel";
    private static final String NORMAL_STATUS_KEY = "NormalRadioButon";
    private static final String ABNORMAL_STATUS_KEY = "AbnormalRadioButton";
    private static final String BIT_ERROR_RATE_TEXT_KEY = "BitErrorRateText";
    /**
     * 发射端和接收端发送的符号
     */
    private BlockingQueue<Packet> TxSymbol;
    private BlockingQueue<Packet> RxSymbol;
    /**
     * 统计误码率变量
     */
    private volatile long lastTotalReceivedSymbol;
    private volatile long lastTotalErrorSymbol;
    /**
     * 误符号率显示
     */
    private volatile static JTextField bitErrorRate;
    /**
     * 状态显示
     */
    private static JRadioButton normal;
    private static JRadioButton abnormal;

    private volatile boolean isRunning = true;

    public CommunicationStatusPart(BlockingQueue<Packet> txSymbol, BlockingQueue<Packet> rxSymbol) {
        TxSymbol = txSymbol;
        RxSymbol = rxSymbol;
    }

    /**
     * 工作状态栏中的内容需要在运行时设置
     * 这里将内部的误码率显示、收发选择和确认按钮都暴露出去
     * @return
     */
    public static Map<String, Object> createStatusPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(DEFAULT_GRID_ROWS,DEFAULT_GRID_COLUMN));
        ButtonGroup buttonGroup = new ButtonGroup();

        // 误码率显示
        JPanel bitErrorRatePanel = new JPanel();
        bitErrorRatePanel.setBackground(Color.WHITE);
        bitErrorRatePanel.setLayout(new GridLayout(1, 2));
        JLabel bitErrorRateLabel = new JLabel("误码率：");
        bitErrorRateLabel.setFont(FontEnum.LABEL_FONT.getFont());
        bitErrorRateLabel.setBackground(Color.WHITE);
        bitErrorRate = new JTextField("0.0");
        bitErrorRate.setFont(FontEnum.TEXTFIELD_FONT.getFont());
        bitErrorRate.setBackground(Color.WHITE);
        bitErrorRate.setEditable(false);
        bitErrorRatePanel.add(bitErrorRateLabel);
        bitErrorRatePanel.add(bitErrorRate);

        // 正常状态显示
        normal = new JRadioButton(NORMAL);
        normal.setBackground(Color.WHITE);
        normal.setFont(FontEnum.RADIOBUTTOBN_FONT.getFont());
        normal.setFont(FontEnum.STATUS_NORMAL_FONT.getFont());
        normal.setEnabled(false);

        // 故障状态显示
        abnormal = new JRadioButton(ABNORMAL);
        abnormal.setBackground(Color.WHITE);
        abnormal.setFont(FontEnum.RADIOBUTTOBN_FONT.getFont());
        abnormal.setFont(FontEnum.STATUS_ABNORMAL_FONT.getFont());
        abnormal.setEnabled(false);

        Border titledBorder = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK),
                                            "工作状态", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
                                            FontEnum.BORDER_TITLE_FONT.getFont());

        buttonGroup.add(normal);
        buttonGroup.add(abnormal);
        panel.add(bitErrorRatePanel);
        panel.add(normal);
        panel.add(abnormal);
        panel.setBorder(titledBorder);
        panel.setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
        panel.setBackground(Color.WHITE);


        Map<String, Object> map = new HashMap<>();
        map.put(PANEL_KEY, panel);
        map.put(NORMAL_STATUS_KEY, normal);
        map.put(ABNORMAL_STATUS_KEY, abnormal);
        map.put(BIT_ERROR_RATE_TEXT_KEY, bitErrorRate);
        return map;
    }

    public static String getPanelKey() {
        return PANEL_KEY;
    }

    public static String getNormalStatusKey() {
        return NORMAL_STATUS_KEY;
    }

    public static String getAbnormalStatusKey() {
        return ABNORMAL_STATUS_KEY;
    }

    public static String getBitErrorRateTextKey() {
        return BIT_ERROR_RATE_TEXT_KEY;
    }

    /**
     *  系统正常状态
     */
    public void setNormalStatus() {
        normal.setForeground(Color.BLUE);
        normal.setBackground(Color.GREEN);
        normal.setSelected(true);
        // 重置故障状态
        abnormal.setForeground(Color.BLACK);
        abnormal.setBackground(Color.WHITE);
    }

    /**
     * 系统关故障状态
     */
    public void setAbnormalStatus() {
        abnormal.setSelected(true);
        abnormal.setBackground(Color.RED);
        abnormal.setForeground(Color.ORANGE);
        // 重置正常状态
        normal.setBackground(Color.WHITE);
        normal.setForeground(Color.BLACK);
    }

    /**
     * 设置误码率数值
     * @param bitErrorRateString 误码率，字符串
     */
    public void setBitErrorRateTextFieldString(String bitErrorRateString) {
        if (bitErrorRateString == null) {
            bitErrorRate.setText("-1");
        }
        bitErrorRate.setText(bitErrorRateString);
    }

    private Double symbolErrorRateCal() {
        int packageCount = 0;
        try {
            while (packageCount <= PACKAGE_PER_CAL && isRunning) {
                Packet TxPacket = TxSymbol.poll(20000, TimeUnit.MILLISECONDS);
                Packet RxPacket = RxSymbol.poll(20000, TimeUnit.MILLISECONDS);
                if (TxPacket != null && RxPacket != null) {
                    float[] symbolTx = new FramingDecoder(TxPacket.data).getTransmittedData();
                    float[] symbolRx = new FramingDecoder(RxPacket.data).getTransmittedData();
                    // 更新接收的符号总数
                    if (lastTotalReceivedSymbol < Long.MAX_VALUE) {
                        lastTotalReceivedSymbol = lastTotalReceivedSymbol + symbolTx.length;
                    } else {
                        lastTotalReceivedSymbol = 1;
                    }
                    // 更新接收帧数总数
                    packageCount = packageCount + 1;
                    // 更新错误符号总数
                    for (int index = 0; index <= symbolTx.length - 1; index++) {
                        if (symbolTx[index] != symbolRx[index]) {
                            if (lastTotalErrorSymbol < Long.MAX_VALUE) {
                                lastTotalErrorSymbol++;
                            } else {
                                lastTotalErrorSymbol = 0;
                            }
                        }
                    }
                } else {
                    if (TxPacket == null) {
                        if (isRunning) {
                            TimedDialog.getDialog("错误", "误码计算时发射端数据提取超时", JOptionPane.ERROR_MESSAGE, false, 0);
                        }
                    }
                    if (RxPacket == null) {
                        if (isRunning) {
                            TimedDialog.getDialog("错误", "误码计算时接收端数据提取超时", JOptionPane.ERROR_MESSAGE, false, 0);
                        }
                    }
                }
            }
            Double res = (lastTotalErrorSymbol+0.0) / lastTotalReceivedSymbol;
            return res;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 误码率计算异常，设为-1
        return new Double(-1);
    }

    @Override
    public void run() {
        // 更新误码数据显示
        while (isRunning) {
            String errorRate = symbolErrorRateCal().toString();
            setBitErrorRateTextFieldString(errorRate);
        }
        //重置误码率显示
        lastTotalErrorSymbol = 0;
        lastTotalReceivedSymbol = 1;
        bitErrorRate.setText("0.0");
    }

    /**
     * 停止误码率计算线程
     */
    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }
}
