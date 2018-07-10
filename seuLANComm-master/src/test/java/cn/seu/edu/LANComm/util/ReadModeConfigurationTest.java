package cn.seu.edu.LANComm.util;

import org.junit.Assert;
import org.junit.Test;
import org.omg.CORBA.PUBLIC_MEMBER;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/26.
 */
public class ReadModeConfigurationTest {

    @Test
    public void TestDQPSK() {
        Map<String, String> dqpsk = ReadModeConfiguration.getCommunicationModeConfigureation("LANComm.properties","DQPSK");
        Map<String, String> dqpskConfig = new HashMap<>();
        dqpskConfig.put("DQPSK-Rb", "[100-bps, 00-kbps, 1-Mbps, 10-Mbps]");
        dqpskConfig.put("DQPSK-Fc", "[1-kHz, 1-MHz, 2-MHz, 10-GHz]");
        dqpskConfig.put("DQPSK-Transmit-Gain", "[1-dBm, 2-dBm, 3-dBm, 10-dBm]");
        dqpskConfig.put("DQPSK-Receive-Gain", "[1-dBm, 2-dBm, 3-dBm, 10-dBm]");
        dqpskConfig.put("DQPSK-Frequence-Offset", "[1-Hz, 2-Hz, 3-kHz]");
        Assert.assertEquals("should be same", dqpsk, dqpskConfig);
    }

    @Test
    public void TestDQPSK_DSSS() {
        Map<String, String> dqpsk_dsss = ReadModeConfiguration.getCommunicationModeConfigureation("LANComm.properties","DQPSK-DSSS");
        Map<String, String> dqpsk_dsssConfig = new HashMap<>();
        dqpsk_dsssConfig.put("DQPSK-DSSS-Rb", "[100-kbps, 1-Mbps, 10-Mbps]");
        dqpsk_dsssConfig.put("DQPSK-DSSS-Fc", "[1-kHz, 1-MHz, 2-MHz, 10-GHz]");
        dqpsk_dsssConfig.put("DQPSK-DSSS-Transmit-Gain", "[1-dBm, 2-dBm, 3-dBm, 10-dBm]");
        dqpsk_dsssConfig.put("DQPSK-DSSS-Receive-Gain", "[1-dBm, 2-dBm, 3-dBm, 10-dBm]");
        dqpsk_dsssConfig.put("DQPSK-DSSS-Frequence-Offset", "[1-Hz, 2-Hz, 3-kHz]");
        Assert.assertEquals("should be same",dqpsk_dsss,dqpsk_dsssConfig);
    }

    @Test
    public void TestDQPSK_FH() {
        Map<String, String> dqpsk_fh = ReadModeConfiguration.getCommunicationModeConfigureation("LANComm.properties","DQPSK-FH");
        Map<String, String> dqpsk_fhConfig = new HashMap<>();
        dqpsk_fhConfig.put("DQPSK-FH-Rb", "[100-kbps, 1-Mbps, 10-Mbps]");
        dqpsk_fhConfig.put("DQPSK-FH-Fc", "[1-kHz, 1-MHz, 2-MHz, 10-GHz]");
        dqpsk_fhConfig.put("DQPSK-FH-Hop", "[100-Hops, 1-kHops]");
        dqpsk_fhConfig.put("DQPSK-FH-Transmit-Gain", "[1-dBm, 2-dBm, 3-dBm, 10-dBm]");
        dqpsk_fhConfig.put("DQPSK-FH-Receive-Gain", "[1-dBm, 2-dBm, 3-dBm, 10-dBm]");
        dqpsk_fhConfig.put("DQPSK-FH-Frequence-Offset", "[1-Hz, 2-Hz, 3-kHz]");
        Assert.assertEquals("should be same", dqpsk_fh, dqpsk_fhConfig);

    }
}
