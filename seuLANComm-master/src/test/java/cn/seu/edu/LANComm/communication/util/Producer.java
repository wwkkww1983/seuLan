package cn.seu.edu.LANComm.communication.util;

import jpcap.JpcapCaptor;
import jpcap.JpcapSender;
import jpcap.NetworkInterface;
import jpcap.packet.EthernetPacket;
import jpcap.packet.Packet;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Administrator on 2018/2/1.
 */
public class Producer implements Runnable{
    private volatile boolean isRunning = true;
    private BlockingQueue queue;
    private static AtomicInteger cout = new AtomicInteger();
    private static final int DEFAULT_RANGE_FOR_SLEEP = 1000;

    public Producer(BlockingQueue queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        String data = null;
        Random random = new Random();
        System.out.println("启动生产者线程");
        try {
            while (isRunning) {
                System.out.println("正在生产数据");
                Thread.sleep(random.nextInt(DEFAULT_RANGE_FOR_SLEEP));
                data = "data: " + cout.incrementAndGet();
                System.out.println("将数据: " + data + " 放入队列");
                if (!queue.offer(data, 2, TimeUnit.SECONDS)) {
                    System.out.println("数据：" + data + " 放入失败");
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        } finally {
            System.out.println("退出生产者线程");
        }
    }

    public void stop() {
        isRunning = false;
    }

    /**
     * 发送MAC帧
     * @param destMAC 目的MAC地址 6 byte
     * @param srcMAC 源MAC地址 6 byte
     * @param frameType 协议类型或长度 4 byte；
     * @param data 数据段， 大于 46 byte 且小于1400 byte
     * @param sender jpcap发送实例
     */
    private static void sendData(byte[] frameType, byte[]destMAC, byte[] srcMAC, byte[] data, JpcapSender sender) {
        if (!checkData(destMAC, srcMAC, frameType, data, sender)) {
            System.out.println("参数不合理");
        }
        // 组装MAC帧
        EthernetPacket ethernetPacket = new EthernetPacket();
        // 同步头、间隔标识、CRC由系统添加，其余用于指定
        ethernetPacket.frametype = ByteArrayConvetor.byteArrayToShort(frameType,0);
        ethernetPacket.src_mac = srcMAC;
        ethernetPacket.dst_mac = destMAC;
        Packet packet = new Packet();
        packet.datalink = ethernetPacket;
        packet.data = data;
        sender.sendPacket(packet);
    }

    /**
     * 校验发送帧的数据长度
     * @param destMAC
     * @param srcMAC
     * @param frameType
     * @param data
     * @param sender
     * @return
     */
    private static boolean checkData(byte[]destMAC, byte[] srcMAC, byte[] frameType, byte[] data, JpcapSender sender) {
        if (destMAC.length != 6) {
            return false;
        }
        if (srcMAC.length != 6) {
            return false;
        }
        if (frameType.length != 4) {
            return false;
        }
        if (data.length <46 || data.length > 1400) {
            return false;
        }
        if (sender == null) {
            return false;
        }
        return true;
    }
}
