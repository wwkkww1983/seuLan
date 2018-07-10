package cn.seu.edu.LANComm.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Administrator on 2018/1/26.
 */
public class CommunicationParameterEnumTest {
    @Test
    public void TestCommunicationParameterEnum() {
        CommunicationParameterEnum Rb = CommunicationParameterEnum.PARAMETER_RB;
        CommunicationParameterEnum Fc = CommunicationParameterEnum.PARAMETER_FC;
        CommunicationParameterEnum transmit_gain = CommunicationParameterEnum.PARAMETER_TRANSMIT_GAIN;
        CommunicationParameterEnum reeive_gain = CommunicationParameterEnum.PARAMETER_RECEIVE_GAIN;
        CommunicationParameterEnum frequence_offset = CommunicationParameterEnum.PARAMETER_FREQUENCE_OFFSET;
        CommunicationParameterEnum hop = CommunicationParameterEnum.PARAMETER_FH_HOP;

        Assert.assertEquals("should be Rb","Rb", Rb.getCommunicationParameter());
        Assert.assertEquals("should be Rb","Fc", Fc.getCommunicationParameter());
        Assert.assertEquals("should be Rb","Transmit-Gain", transmit_gain.getCommunicationParameter());
        Assert.assertEquals("should be Rb","Receive-Gain", reeive_gain.getCommunicationParameter());
        Assert.assertEquals("should be Rb","Frequence-Offset", frequence_offset.getCommunicationParameter());
        Assert.assertEquals("should be Rb","Hop", hop.getCommunicationParameter());
    }
}
