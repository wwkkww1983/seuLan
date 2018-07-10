package cn.seu.edu.LANComm.communication.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Administrator on 2018/2/4.
 */
public class FrameCoderAndDecoderTest {

    @Test
    public void test() {
        DataLinkParameterEnum parameterIDentifier = DataLinkParameterEnum.PARAMETER_SETTING;
        float[] data = new float[]{3.14f, 1.25f, 1.11f, 1.123f};
        byte[] bytes = FramingEncoder.getByteToSend(parameterIDentifier, data);

        FramingDecoder decoder = new FramingDecoder(bytes);
        DataLinkParameterEnum parameterRx = decoder.getParameterIDentifier();
        int dataLen = decoder.getDataLen();
        float[] dataRx = decoder.getTransmittedData();
        Assert.assertEquals("should be same",parameterRx, DataLinkParameterEnum.PARAMETER_SETTING);
        Assert.assertEquals("should be same", data.length, dataLen);
        Assert.assertArrayEquals(data, dataRx, 1E-10F);
    }
}
