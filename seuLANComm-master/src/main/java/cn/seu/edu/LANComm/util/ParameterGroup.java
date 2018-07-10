package cn.seu.edu.LANComm.util;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 对 LANComm.properties 中的参数配置进行分组
 * Created by Administrator on 2018/1/26.
 * @author WYCPhoenix
 * @date 2018-1-26-20:41
 */
public class ParameterGroup {
    private static final String KV_SEPARATOR = "-";
    private static final String KVPAIR_SEPARATOR = ",";

    /**
     * outer.map的key为配置文件的参数名
     * outer.map的value部分为 值-单位
     * inner.map的key为单位
     * inner.map的value为值
     * @param rowMap 原始的Map文件
     *               value的形式[值-单位,值-单位,....]
     * @return
     */
    public static Map<String, Map<String, List<Float>>> groupByUnit(Map<String, String> rowMap) {
        Map<String, Map<String, List<Float>>> result = new HashMap<>();
        Set<Map.Entry<String, String>> entries = rowMap.entrySet();
        Iterator<Map.Entry<String, String>> entryIterator = entries.iterator();
        while (entryIterator.hasNext()) {
            Map.Entry<String, String> entry = entryIterator.next();
            String key = entry.getKey();
            String tempValues = entry.getValue();
            // 去除首尾的[]
            int len = tempValues.length();
            String[] kvPairs = tempValues.substring(1, len - 1).split(KVPAIR_SEPARATOR);
            Map<String, List<Float>> innerMap = new HashMap<>();
            for (String kvPair : kvPairs) {
                String[] tempKV = kvPair.split(KV_SEPARATOR);
                String kvPairUnit = tempKV[1].trim();
                Float kvPairValue = Float.parseFloat(tempKV[0].trim());
                if (!innerMap.containsKey(kvPairUnit)) {
                    List<Float> tempList = new ArrayList<>();
                    tempList.add(kvPairValue);
                    innerMap.put(kvPairUnit, tempList);
                } else {
                    innerMap.get(kvPairUnit).add(kvPairValue);
                }
            }
            result.put(key, innerMap);
        }
        return result;
    }

    /**
     * outer.map的key为配置文件的参数名
     * outer.map的value部分为 值-单位
     * inner.map的key为值
     * inner.map的value为单位
     * @param rowMap 原始的Map文件
     *               value的形式[值-单位,值-单位,....]
     * @return
     */
    public static Map<String, Map<Float, List<String>>> groupByNumber(Map<String, String> rowMap) {
        Map<String, Map<Float, List<String>>> result = new HashMap<>();
        Set<Map.Entry<String, String>> entries = rowMap.entrySet();
        Iterator<Map.Entry<String, String>> entryIterator = entries.iterator();
        while (entryIterator.hasNext()) {
            Map.Entry<String, String> entry = entryIterator.next();
            String key = entry.getKey();
            String tempValues = entry.getValue();
            // 去除首尾的[]
            int len = tempValues.length();
            String[] kvPairs = tempValues.substring(1, len - 1).split(KVPAIR_SEPARATOR);
            Map<Float, List<String>> innerMap = new HashMap<>();
            for (String kvPair : kvPairs) {
                String[] tempKV = kvPair.split(KV_SEPARATOR);
                String kvPairUnit = tempKV[1].trim();
                Float kvPairValue = Float.parseFloat(tempKV[0].trim());
                if (!innerMap.containsKey(kvPairValue)) {
                    List<String> tempList = new ArrayList<>();
                    tempList.add(kvPairUnit);
                    innerMap.put(kvPairValue, tempList);
                } else {
                    innerMap.get(kvPairValue).add(kvPairUnit);
                }
            }
            result.put(key, innerMap);
        }
        return result;
    }

    public static void main(String[] args) {
        Map<String, String> dqpskConfig = new HashMap<>();
        dqpskConfig.put("DQPSK-Rb", "[100-bps, 00-kbps, 1-Mbps, 10-Mbps]");
        dqpskConfig.put("DQPSK-Fc", "[1-kHz, 1-MHz, 2-MHz, 10-GHz]");
        dqpskConfig.put("DQPSK-Transmit-Gain", "[1-dBm, 2-dBm, 3-dBm, 10-dBm]");
        dqpskConfig.put("DQPSK-Receive-Gain", "[1-dBm, 2-dBm, 3-dBm, 10-dBm]");
        dqpskConfig.put("DQPSK-Frequence-Offset", "[1-Hz, 2-Hz, 3-kHz]");

        Map<String, Map<String, List<Float>>> res = ParameterGroup.groupByUnit(dqpskConfig);
        System.out.println(res);
        Map<String, Map<Float, List<String>>> res2 = ParameterGroup.groupByNumber(dqpskConfig);
        System.out.println(res2);
    }
}
