package cn.seu.edu.LANComm.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Administrator on 2018/4/20.
 */
public class TestReadControllerConstants {
    @Test
    public void test() {
        String constellation_len = ReadControllerConstants.getConstellationDataLenShowed();
        String constellation_upt = ReadControllerConstants.getConstellationUpdateInterval();
        Assert.assertEquals("3", constellation_len);
        Assert.assertEquals("1", constellation_upt);

        String hoppingPattern_len = ReadControllerConstants.getHoppingPatternDataLenShowed();
        String hoppingPattern_upt = ReadControllerConstants.getHoppingPatternUpdateInterval();
        Assert.assertEquals("100", hoppingPattern_len);
        Assert.assertEquals("10", hoppingPattern_upt);

        String intermediateFFT_len = ReadControllerConstants.getIntermediateFrequencyFFTDataLenShowed();
        String intermediateFFT_upt = ReadControllerConstants.getIntermediateFrequencyFFTUpdateInterval();
        Assert.assertEquals("1000", intermediateFFT_len);
        Assert.assertEquals("100", intermediateFFT_upt);

        String intermediate_len = ReadControllerConstants.getIntermediateFrequencyDataLenShowed();
        String intermediate_upt = ReadControllerConstants.getIntermediateFrequencyUpdateInterval();
        Assert.assertEquals("1000", intermediate_len);
        Assert.assertEquals("50", intermediate_upt);
    }
}
