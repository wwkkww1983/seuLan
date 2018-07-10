package cn.seu.edu.LANComm.util;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import jdk.nashorn.internal.objects.NativeUint8Array;
import jdk.nashorn.internal.scripts.JO;
import jpcap.JpcapWriter;
import org.omg.CORBA.PUBLIC_MEMBER;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * dump文件写入工具类
 * Created by Administrator on 2018/3/18.
 * @author WYCPhoenix
 */
public class DumpFileUtil {
    private static final String INTERMEDIATEFREQUENCY_DATA = "IntermediateFrequencyData.log";
    private static final String CONSTELLATION_DATA = "ConstellationData.log";
    private static final String HOPPING_PATTERN_DATA = "HoppingPatternData.log";
    private static final String TRANSMITTED_SYMBOL = "TransmittedSymbol.log";
    private static final String RECEIVED_SYMBOL = "ReceivedData.log";

    /**
     * 获取程序所在位置
     * @return
     */
    private  static String getParentFolderPath() {
        String path = System.getProperty("user.dir").replace('\\', '/');
        SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        String dateString = format.format(new Date());
        return path + "/" +dateString;
    }

    /**
     * 获取各个数据文件的位置
     * @return
     */
    public static Map<String, String> getDumFilePath() {
        String parentPath = getParentFolderPath();
        File file = new File(parentPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        Map<String, String> pathMap = new HashMap<>(5);
        pathMap.put(INTERMEDIATEFREQUENCY_DATA, parentPath + "/" + INTERMEDIATEFREQUENCY_DATA);
        createFile(parentPath + "/" + INTERMEDIATEFREQUENCY_DATA);
        pathMap.put(CONSTELLATION_DATA, parentPath + "/" + CONSTELLATION_DATA);
        createFile(parentPath + "/" + CONSTELLATION_DATA);
        pathMap.put(HOPPING_PATTERN_DATA, parentPath + "/" + HOPPING_PATTERN_DATA);
        createFile(parentPath + "/" + HOPPING_PATTERN_DATA);
        pathMap.put(TRANSMITTED_SYMBOL, parentPath + "/" + TRANSMITTED_SYMBOL);
        createFile(parentPath + "/" + TRANSMITTED_SYMBOL);
        pathMap.put(RECEIVED_SYMBOL, parentPath + "/" + RECEIVED_SYMBOL);
        createFile(parentPath + "/" + RECEIVED_SYMBOL);

        return pathMap;
    }

    /**
     * 根据文件名创建文件，不存在则创建，存在则不做任何事
     * @param fileName
     */
    public static void createFile(String fileName) {
        File file = new File(fileName);
        if (file.exists()) {
            return;
        } else {
            try {
                file.createNewFile();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "文件创建错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    public static void main(String[] args){
        Map<String, String> pathMap = getDumFilePath();
        System.out.println(pathMap.toString());

    }
}
