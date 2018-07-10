package cn.seu.edu.LANComm.communication.util;

import org.junit.Assert;
import static cn.seu.edu.LANComm.communication.util.MACStringConvertor.*;
import org.junit.Test;

/**
 * Created by Administrator on 2018/1/31.
 */
public class MACStringConvertorTest {

    @Test
    public void testStringToMac() {
        String macString = "00-23-24-B0-96-A7";
        byte[] macBytes = stringToMAC(macString);
        String actual = macToString(macBytes);
        Assert.assertEquals(macString, actual);
        System.out.println(macToString(stringToMAC(macString)));
    }
}
