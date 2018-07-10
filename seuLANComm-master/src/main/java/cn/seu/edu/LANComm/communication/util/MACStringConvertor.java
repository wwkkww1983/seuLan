package cn.seu.edu.LANComm.communication.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2018/1/31.
 */
public class MACStringConvertor {

    /**
     * 将 6 个字节的 MAC 地址转为标准形式MAC字符串
     * @param macAddress
     * @return
     */
    public static String macToString(byte[] macAddress) {
        if (macAddress == null ||macAddress.length != 6) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < macAddress.length; i++) {
            String temp = Integer.toHexString(macAddress[i] & 0XFF);
            if (temp.length() == 1) {
                stringBuilder.append("0" + temp);
            } else {
                stringBuilder.append(temp);
            }
            if (i != macAddress.length - 1) {
                stringBuilder.append("-");
            }
        }
        return stringBuilder.toString().toUpperCase();
    }

    /**
     * 将标准的字符串形式MAC转为6字节的MAC地址
     * @param macString
     * @return
     */
    public static byte[] stringToMAC(String macString) {
        if (!checkMAC(macString)) {
            return null;
        }
        byte[] mac = new byte[6];
        String[] macPartition = macString.split("\\-");
        for (int i = 0; i < macPartition.length; i++) {
            mac[i] = (byte) Integer.parseInt(macPartition[i], 16);
        }
        return mac;
    }

    /**
     * 校验MAC地址的合法性，
     * 标砖MAC表示：XX-XX-XX-XX-XX-XX
     * @param macString 待校验的MAC
     * @return
     */
    public static boolean checkMAC(String macString) {
        String macPattern = "([A-Fa-f0-9]{2}-){5}[A-Fa-f0-9]{2}";
        Pattern pattern = Pattern.compile(macPattern);
        Matcher matcher = pattern.matcher(macString);
        return matcher.matches();
    }
}
