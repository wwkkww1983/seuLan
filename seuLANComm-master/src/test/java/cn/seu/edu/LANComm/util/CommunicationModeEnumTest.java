package cn.seu.edu.LANComm.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Administrator on 2018/1/26.
 */
public class CommunicationModeEnumTest {
    @Test
    public void TestCommunicationModeEnum() {
        CommunicationModeEnum dqpsk = CommunicationModeEnum.DQOSK_MODE;
        Assert.assertEquals("should be DQPSK", "DQPSK", dqpsk.getCommunicationMode());
        CommunicationModeEnum dqpsk_dsss = CommunicationModeEnum.DQOSK_DSSS_MODE;
        Assert.assertEquals("should be DQPSK-DSSS", "DQPSK-DSSS", dqpsk_dsss.getCommunicationMode());
        CommunicationModeEnum dqpsk_fh = CommunicationModeEnum.DQOSK_FH_MODE;
        Assert.assertEquals("should be DQPSK-FH", "DQPSK-FH", dqpsk_fh.getCommunicationMode());
    }
}
