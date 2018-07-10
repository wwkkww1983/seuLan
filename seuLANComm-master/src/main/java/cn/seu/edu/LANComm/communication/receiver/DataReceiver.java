package cn.seu.edu.LANComm.communication.receiver;

import cn.seu.edu.LANComm.communication.util.NetworkInterfaceUtil;
import cn.seu.edu.LANComm.ui.TimedDialog;
import jpcap.JpcapCaptor;
import jpcap.PacketReceiver;

import javax.swing.*;
import java.io.IOException;

/**
 * 各类数据接收线程
 * Created by Administrator on 2018/3/13.
 * @author WYCPhoenox
 */
public class DataReceiver implements Runnable{
    private String localMAC;
    private String filter;
    private JpcapCaptor captor;
    private PacketReceiver dispatcher;

    public DataReceiver(String localMAC, String filter, PacketReceiver dispatcher) {
        this.localMAC = localMAC;
        this.filter = filter;
        this.dispatcher = dispatcher;
        this.captor = getJpcapCaptor();
    }

    /**
     * 为避免循环引用问题，先采用这种黑魔法吧，虽然很不好
     * @return
     */
    private JpcapCaptor getJpcapCaptor() {
        JpcapCaptor captor = null;
        jpcap.NetworkInterface devicesUsed = NetworkInterfaceUtil.getDesignateDeviceByMACString(localMAC);
        if (devicesUsed != null) {
            try {
                captor = JpcapCaptor.openDevice(devicesUsed, 4000, false, 10000);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            TimedDialog.getDialog("错误","网卡打开失败，请确认MAC地址或重启启动程序", JOptionPane.ERROR_MESSAGE, false,0);
        }
        return captor;
    }

    @Override
    public void run() {
        captor = getJpcapCaptor();
        try {
            captor.setFilter(filter, true);
            captor.loopPacket(-1, dispatcher);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getLocalMAC() {
        return localMAC;
    }

    public void setLocalMAC(String localMAC) {
        this.localMAC = localMAC;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public JpcapCaptor getCaptor() {
        return captor;
    }

    public void setCaptor(JpcapCaptor captor) {
        this.captor = captor;
    }

    public PacketReceiver getDispatcher() {
        return dispatcher;
    }

    public void setDispatcher(PacketReceiver dispatcher) {
        this.dispatcher = dispatcher;
    }
}
