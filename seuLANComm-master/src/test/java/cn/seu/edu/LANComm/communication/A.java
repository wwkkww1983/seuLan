package cn.seu.edu.LANComm.communication;

import jpcap.JpcapCaptor;
import jpcap.NetworkInterface;
import jpcap.NetworkInterfaceAddress;

/**
 * Created by Administrator on 2018/1/29.
 */
public class A {
    public static void main(String[] args) {
        // 获取网卡列表
        NetworkInterface[] devices = JpcapCaptor.getDeviceList();
        System.out.println(devices.length);
        for (int i = 0; i < devices.length; i++) {
            // 设备号，网卡名，网卡描述
            System.out.println("设备号，网卡名，网卡描述");
            System.out.println("设备号 " + i + " : " +
                    devices[i].name + "(" + devices[i].description + ")");
            // 网卡所处的数据链路层名称与描述
            System.out.println("dataLink: " + devices[i].datalink_name + "（" +
                            devices[i].datalink_description + "）");
            // 网卡MAC地址
            System.out.println("MAC Address");
            for (byte b : devices[i].mac_address) {
                System.out.print(Integer.toHexString(b & 0xff) + " : ");
            }
            System.out.println();
            // 输出网卡的IPV4 IPV6地址
            for (NetworkInterfaceAddress address : devices[i].addresses) {
                System.out.println("输出网卡的IPV4 IPV6地址");
                System.out.println("address : " + address.address + " " +
                            address.subnet + " " + address.broadcast);
            }
        }
    }
}
