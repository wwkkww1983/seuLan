package cn.seu.edu.LANComm.util;

import com.sun.org.apache.bcel.internal.generic.NEW;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/26.
 */
public class ParameterGroupTest {

    @Test
    public void TestGroupByUnit() {
        Map<String, String> dqpskConfig = new HashMap<>();
        dqpskConfig.put("DQPSK-Rb", "[100-bps, 00-kbps, 1-Mbps, 10-Mbps]");
        dqpskConfig.put("DQPSK-Fc", "[1-kHz, 1-MHz, 2-MHz, 10-GHz]");
        dqpskConfig.put("DQPSK-Transmit-Gain", "[1-dBm, 2-dBm, 3-dBm, 10-dBm]");
        dqpskConfig.put("DQPSK-Receive-Gain", "[1-dBm, 2-dBm, 3-dBm, 10-dBm]");
        dqpskConfig.put("DQPSK-Frequence-Offset", "[1-Hz, 2-Hz, 3-kHz]");
        Map<String, Map<String, List<Float>>> actual = ParameterGroup.groupByUnit(dqpskConfig);
        Map<String, Map<String, List<Float>>> expected = new HashMap<>();

        List<Float> bpsList = new ArrayList<>();
        Map<String, List<Float>> innerMapRb = new HashMap<>();
        bpsList.add(new Float(100));
        innerMapRb.put("bps", bpsList);
        List<Float> kbpsList = new ArrayList<>();
        kbpsList.add(new Float(00));
        innerMapRb.put("kbps", kbpsList);
        List<Float> MbpsList = new ArrayList<>();
        MbpsList.add(new Float(1));
        MbpsList.add(new Float(10));
        innerMapRb.put("Mbps", MbpsList);
        expected.put("DQPSK-Rb", innerMapRb);

        List<Float> kHzList = new ArrayList<>();
        Map<String, List<Float>> innerMapFc = new HashMap<>();
        kHzList.add(new Float(1));
        innerMapFc.put("kHz", kHzList);
        List<Float> MHzList = new ArrayList<>();
        MHzList.add(new Float(1));
        MHzList.add(new Float(2));
        innerMapFc.put("MHz", MHzList);
        List<Float> GHzList = new ArrayList<>();
        GHzList.add(new Float(10));
        innerMapFc.put("GHz", GHzList);
        expected.put("DQPSK-Fc", innerMapFc);

        List<Float> dBmListTG = new ArrayList<>();
        Map<String, List<Float>> innerMapTG = new HashMap<>();
        dBmListTG.add(new Float(1));
        dBmListTG.add(new Float(2));
        dBmListTG.add(new Float(3));
        dBmListTG.add(new Float(10));
        innerMapTG.put("dBm", dBmListTG);
        expected.put("DQPSK-Transmit-Gain", innerMapTG);

        List<Float> dBmListRG = new ArrayList<>();
        Map<String, List<Float>> innerMapRG = new HashMap<>();
        dBmListRG.add(new Float(1));
        dBmListRG.add(new Float(2));
        dBmListRG.add(new Float(3));
        dBmListRG.add(new Float(10));
        innerMapRG.put("dBm", dBmListRG);
        expected.put("DQPSK-Receive-Gain", innerMapRG);

        List<Float> offsetHzList = new ArrayList<>();
        Map<String, List<Float>> innerMapOffset = new HashMap<>();
        offsetHzList.add(new Float(1));
        offsetHzList.add(new Float(2));
        innerMapOffset.put("Hz", offsetHzList);
        List<Float> offsetkHzList = new ArrayList<>();
        offsetkHzList.add(new Float(3));
        innerMapOffset.put("kHz", offsetkHzList);
        expected.put("DQPSK-Frequence-Offset", innerMapOffset);
        System.out.println(actual);
        System.out.println(expected);

        Assert.assertEquals("should be same", expected, actual);
    }

}
