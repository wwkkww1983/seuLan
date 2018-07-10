package cn.seu.edu.LANComm.communication.dispatcher;

import cn.seu.edu.LANComm.communication.util.DataLinkParameterEnum;
import cn.seu.edu.LANComm.communication.util.FramingDecoder;
import cn.seu.edu.LANComm.ui.TimedDialog;
import jpcap.JpcapCaptor;
import jpcap.JpcapWriter;
import jpcap.PacketReceiver;
import jpcap.packet.EthernetPacket;
import jpcap.packet.Packet;

import javax.swing.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 发送端发送符号数据分发队列
 * Created by Administrator on 2018/3/13.
 * @author WYCPhonenix
 */
public class TransmittedSymbolPacketDispatcher implements PacketReceiver{
    private static final TimeUnit OFFER_TIMEOUT_UNIT = TimeUnit.MILLISECONDS;
    private long offerTimeout;
    private BlockingQueue<Packet> data;
    private JpcapWriter writer;
    private JpcapCaptor captor;
    private volatile boolean isRunning = true;

    public TransmittedSymbolPacketDispatcher(long offerTimeout, BlockingQueue<Packet> data) {
        this.offerTimeout = offerTimeout;
        this.data = data;
    }

    @Override
    public void receivePacket(Packet packet) {
        EthernetPacket ethernetPacket = (EthernetPacket) packet.datalink;
        if (ethernetPacket.frametype == Short.parseShort(DataLinkParameterEnum.FRAME_TYPE.getDataType())) {
            FramingDecoder decoder = new FramingDecoder(packet.data);
            if (decoder.getParameterIDentifier().getDataType().equals(DataLinkParameterEnum.TRANSMITTED_SYMBOL_DATA.getDataType())) {
                writer = getWriter();
                if (writer != null) {
                    writer.writePacket(packet);
                }
                try {
                    boolean success = data.offer(packet, offerTimeout, OFFER_TIMEOUT_UNIT);
                    if (!success) {
                        if (isRunning) {
                            TimedDialog.getDialog("错误", "发送端发送符号数据队列满，队列没有消费", JOptionPane.ERROR_MESSAGE, false, 0);
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        if (!isRunning) {
            captor = getCaptor();
            if (captor != null) {
                captor.breakLoop();
            }
        }
    }

    public long getOfferTimeout() {
        return offerTimeout;
    }

    public void setOfferTimeout(long offerTimeout) {
        this.offerTimeout = offerTimeout;
    }

    public BlockingQueue<Packet> getData() {
        return data;
    }

    public void setData(BlockingQueue<Packet> data) {
        this.data = data;
    }

    public JpcapWriter getWriter() {
        return writer;
    }

    public void setWriter(JpcapWriter writer) {
        this.writer = writer;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public JpcapCaptor getCaptor() {
        return captor;
    }

    public void setCaptor(JpcapCaptor captor) {
        this.captor = captor;
    }
}
