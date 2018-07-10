package cn.seu.edu.LANComm.communication.util;

import jpcap.JpcapCaptor;
import jpcap.NetworkInterface;

/**
 * Created by Administrator on 2018/2/4.
 */
public class NetworkInterfaceUtil {

    /**
     * 获取本地所有的MAC 地址
     * @return
     */
    public static String[] getAllMACAddress() {
        NetworkInterface[] devices = JpcapCaptor.getDeviceList();
        String[] macStrings = new String[devices.length];
        for (int index = 0; index < devices.length; index++) {
            macStrings[index] = MACStringConvertor.macToString(devices[index].mac_address);
        }
        return macStrings;
    }

    /**
     * 根据指定的MAC地址，返回对应的设备
     * @param MACString 字符串形式的MAC地址
     * @return 对应的本地设备
     */
    public static NetworkInterface getDesignateDeviceByMACString(String MACString) {
        if (!MACStringConvertor.checkMAC(MACString)) {
            return null;
        }
        NetworkInterface res = null;
        NetworkInterface[] devices = JpcapCaptor.getDeviceList();
        for (NetworkInterface device : devices) {
            if (MACStringConvertor.macToString(device.mac_address).equals(MACString)) {
                res = device;
                break;
            }
        }
        return res;
    }
}
