package cn.seu.edu.LANComm.simulator;

import cn.seu.edu.LANComm.communication.EthernetPacketSender;
import cn.seu.edu.LANComm.communication.util.DataLinkParameterEnum;
import cn.seu.edu.LANComm.communication.util.FramingDecoder;
import cn.seu.edu.LANComm.communication.util.MACStringConvertor;
import cn.seu.edu.LANComm.communication.util.NetworkInterfaceUtil;
import cn.seu.edu.LANComm.util.CommunicationModeEnum;
import jpcap.JpcapCaptor;
import jpcap.NetworkInterface;
import jpcap.PacketReceiver;
import jpcap.packet.EthernetPacket;
import jpcap.packet.Packet;

import java.io.IOException;
import java.util.EnumSet;
import java.util.Iterator;

/**
 * Created by Administrator on 2018/3/12.
 */
public class Rx implements PacketReceiver, Runnable{
    private static long packetCount = 0;
    static JpcapCaptor captor = null;
    private static final String FILE_PATH = "C:\\Users\\Administrator\\Desktop"
            + "\\TxHost\\TxHost\\src\\data\\sinData.txt";
    private static volatile boolean stopSend = false;
    private static final String localMAC = "00-0C-29-62-C7-C8".trim();
    private static final String destMAC = "00:23:24:B0:96:A7".trim();
    private static final String filter = "ether src 00:23:24:B0:96:A7";

    @SuppressWarnings("all")
    public static void main(String[] args) {
        // step1 等待接收配置数据
        NetworkInterface deviceUsed = NetworkInterfaceUtil.getDesignateDeviceByMACString(localMAC);
        if (deviceUsed != null) {
            try {
                while (true) {
                    // step1 等待接收配置数据
                    System.out.println("等待接收配置数据...");
                    captor = JpcapCaptor.openDevice(deviceUsed, 2000, false, 2000);
                    captor.setFilter(filter, true);
                    // loopPacket不受超时影响
                    captor.loopPacket(-1, new Rx());
                    System.out.println("收到接收配置数据！");

                    // step2 发送中频采样率
                    System.out.println("发送中频采样率开始");
                    EthernetPacketSender.sendEthernetPacket(new String[]{destMAC}, localMAC, DataLinkParameterEnum.SAMPLE_RATE, new float[]{200});
                    System.out.println("中频采样率发送结束");

                    // step3 等到接收通信开始指令
                    System.out.println("等待通信开始指令...");
                    captor.setFilter(filter, true);
                    captor.loopPacket(-1, new Rx());
                    System.out.println("通信开始指令收到!");

                    // step4 延时 2s，等待上位机完成初始化
                    Thread.sleep(2000);

                    // step5 发送通信数据
                    System.out.println("正在发送通信数据");
                    float[] intermediateData = ReadDataFile.readData(FILE_PATH);
                    while (!stopSend) {
                        System.out.println("正在发送第 " + packetCount + " 个中频采样信号数据包");
                        EthernetPacketSender.sendEthernetPacket(new String[]{destMAC}, localMAC, DataLinkParameterEnum.INTERMEDIATE_DATA, intermediateData);

                        System.out.println("第 " + packetCount + " 个中频采样信号数据包发送结束");
                        // 间隔一定时间发送
                        Thread.sleep(200);
                    }
                    System.out.println("通信数据停止发送，等待接收新的参数配置信息...");
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                if (captor != null) {
                    captor.close();
                }
            }
        }
    }

    @Override
    @SuppressWarnings("all")
    public void receivePacket(Packet packet) {
        EthernetPacket ethernetPacket = (EthernetPacket) packet.datalink;
        if (Short.parseShort(DataLinkParameterEnum.FRAME_TYPE.getDataType()) == ethernetPacket.frametype) {
            System.out.println("帧类型：" + ethernetPacket.frametype);
            System.out.println("目的MAC：" + MACStringConvertor.macToString(ethernetPacket.dst_mac));
            System.out.println("源MAC：" + MACStringConvertor.macToString(ethernetPacket.src_mac));
            FramingDecoder decoder = new FramingDecoder(packet.data);
            System.out.println("数据类型：" + decoder.getParameterIDentifier());
            if (!decoder.getParameterIDentifier().getDataType().equals(DataLinkParameterEnum.PARAMETER_SETTING.getDataType())) {
                System.out.println(decoder.getParameterIDentifier().toString());
                showArray(decoder.getTransmittedData());
            } else {
                // 这时参数配置包，显示参数配置详细信息
                float[] data = decoder.getTransmittedData();
                CommunicationModeEnum modeEnum  = getModeEnum((int) data[0]);
                showParaSetted(data, modeEnum);
            }
            captor.breakLoop();
        }
    }

    @SuppressWarnings("all")
    private void showArray(float[] data) {
        if (data == null) {
            System.out.println("data字段异常");
        }
        if (data.length == 0) {
            System.out.println("数据字段为空");
        }
        for (int i = 0; i < data.length; i++) {
            System.out.print(data[i] + " ");
        }
        System.out.println();
    }

    @SuppressWarnings("all")
    private void showParaSetted(float[] data, CommunicationModeEnum modeEnum) {
        if (data == null || data.length == 0) {
            System.out.println("参数配置字段异常");
        }
        if (modeEnum.getModeCode() == CommunicationModeEnum.DQOSK_FH_MODE.getModeCode()
                && data.length != 7) {
            System.out.println(modeEnum.toString() + " 参数配置异常");
            showArray(data);
        } else if (data.length != 6) {
            System.out.println(modeEnum.toString() + " 参数配置异常");
            showArray(data);
        }
        System.out.println("通信模式：" + modeEnum);
        System.out.println("码元速率Rb：" + data[1]);
        System.out.println("载波频率Fc：" + data[2]);
        System.out.println("频偏Fo：" + data[3]);
        System.out.println("发射增益Tg：" + data[4]);
        System.out.println("接收增益Rg：" + data[5]);
        if (modeEnum.getModeCode() == CommunicationModeEnum.DQOSK_FH_MODE.getModeCode()) {
            System.out.println("跳变速率hops：" + data[6]);
        }
    }

    @SuppressWarnings("all")
    private CommunicationModeEnum getModeEnum(int modeCode) {
        CommunicationModeEnum modeEnum = null;
        EnumSet<CommunicationModeEnum> modeEnums = EnumSet.allOf(CommunicationModeEnum.class);
        Iterator<CommunicationModeEnum> iterator = modeEnums.iterator();
        while (iterator.hasNext()) {
            CommunicationModeEnum temp = iterator.next();
            if (temp.getModeCode() == modeCode) {
                modeEnum = temp;
                break;
            }
        }
        return modeEnum;
    }

    @Override
    @SuppressWarnings("all")
    public void run() {
        // 侦听通信停止信号
        NetworkInterface deviceUsed = NetworkInterfaceUtil.getDesignateDeviceByMACString(localMAC);
        JpcapCaptor captor= null;
        if (deviceUsed != null) {
            try {
                captor = JpcapCaptor.openDevice(deviceUsed, 2000, false, 2000);
                captor.setFilter(filter, true);
                captor.loopPacket(-1, new PacketReceiver() {
                    @Override
                    public void receivePacket(Packet packet) {
                        EthernetPacket ethernetPacket = (EthernetPacket) packet.datalink;
                        if (Short.parseShort(DataLinkParameterEnum.FRAME_TYPE.getDataType()) == ethernetPacket.frametype) {
                            FramingDecoder decoder = new FramingDecoder(packet.data);
                            if (DataLinkParameterEnum.COMMUNICATION_STOP.getDataType().equals(decoder.getParameterIDentifier().getDataType())) {
                                System.out.println("收到停止信号");
                                stopSend = true;
                            }
                        }
                    }
                });
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

