package cn.seu.edu.LANComm.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/27.
 */
public class ExtendStringToSameLength {

    public static List<String> extendString(List<String> stringList) {
        int maxLen = getMaxStringLength(stringList);
        List<String> res = new ArrayList<>();
        for (String string : stringList) {
            String temp = extendString(string, maxLen);
            res.add(temp);
        }
        return res;
    }

    /**
     * 将一个字符串扩展为一个长度为len的字符串
     * 如果原字符串的长度大于等于len，则返回原字符串
     * 如果原字符串长度小于len，则用空格补全
     * @param string
     * @param len
     * @return
     */
    private static String extendString(String string, int len) {
        if (string == null || string.length() >= len) {
            return string;
        }
        int originLen = string.length();
        StringBuilder stringBuilder = new StringBuilder(string);
        for (int i = 0; i < len - originLen; i++) {
            stringBuilder.append(" ");
        }
        return stringBuilder.toString();
    }

    /**
     * 获取字符串列表中字符串最大长度
     * @param stringList
     * @return
     */
    private static int getMaxStringLength(List<String> stringList) {
        if (stringList == null || stringList.size() <= 0) {
            System.out.println("空列表");
        }
        int maxLen = 0;
        for (String string : stringList) {
            int temp = string.length();
            if (temp >= maxLen) {
                maxLen = temp;
            }
        }
        return maxLen;
    }
}
