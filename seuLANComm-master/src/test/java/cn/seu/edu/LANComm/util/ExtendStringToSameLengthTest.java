package cn.seu.edu.LANComm.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/27.
 */
public class ExtendStringToSameLengthTest {

    @Test
    public  void testExtendString() {
        List<String> stringList = new ArrayList<>();
        stringList.add("DQPSK");
        stringList.add("DQPSK-DSSS");
        stringList.add("DQPSK-FH");
        List<String> actual = ExtendStringToSameLength.extendString(stringList);
        List<String> expected = new ArrayList<>();
        expected.add("DQPSK     ");
        expected.add("DQPSK-DSSS");
        expected.add("DQPSK-FH  ");

        Assert.assertEquals("should be same",actual, expected);
    }
}
