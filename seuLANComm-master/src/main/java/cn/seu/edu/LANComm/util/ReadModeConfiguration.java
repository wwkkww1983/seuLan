package cn.seu.edu.LANComm.util;

/**
 * Created by Administrator on 2018/1/26.
 */

import java.io.*;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * 为获得配置上的灵活性，工作模式、参数设置部分的信息，
 * 将会从名为 LANComm.properties文件读取，且该文件仅在
 * 软件启动时读取一次，后序更改在下次启动后生效
 * @author WYCPhoenix
 * @date 2011-26-16:54
 */
public class ReadModeConfiguration {
    /**
     * LANComm.properties中key的分隔符
     */
    private static final String KEY_SEPARATOR = "-";
    private static Properties properties;

    synchronized private static void loadPropertiesFile(String propertyFileName) {
        properties = new Properties();
        InputStream inputStream = null;

        try {
            String path = System.getProperty("user.dir").replace('\\', '/') + "/config/" + propertyFileName;
            inputStream = new BufferedInputStream(new FileInputStream(path));
            properties.load(inputStream);
        } catch (FileNotFoundException e) {
            System.out.println("文件不存在");
        } catch (IOException e) {
            System.out.println("IOException");
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                // you can do nothing!
            }
        }
    }

    /**
     * 从 LANComm.properties 文件中根据通信模式提取对应的配置
     * @param propertyFileName 配置文件名
     * @param communicationMode 通信模式
     * @return 通信配置对应的配置
     */
    public static Map<String, String> getCommunicationModeConfigureation(String propertyFileName, String communicationMode) {
        Map<String, String> configuration = new HashMap<>();
        // 加载配置文件
        loadPropertiesFile(propertyFileName);
        // 迭代 CommunicationModeEnum 类
        EnumSet<CommunicationModeEnum> communicationModeEnumEnumSet = EnumSet.allOf(CommunicationModeEnum.class);
        Iterator<CommunicationModeEnum> communicationModeEnumIterator = communicationModeEnumEnumSet.iterator();
        String modeName = null;
        while (communicationModeEnumIterator.hasNext()) {
            CommunicationModeEnum modeEnum = communicationModeEnumIterator.next();
            if (modeEnum.getCommunicationMode().equals(communicationMode)) {
                modeName = modeEnum.getCommunicationMode();
            }
        }
        if (modeName == null) {
            System.out.println("communicationMode 输入错误");
        }

        // 迭代 CommunicationParameterEnum 类
        EnumSet<CommunicationParameterEnum> communicationParameterEnumEnumSet = EnumSet.allOf(CommunicationParameterEnum.class);
        Iterator<CommunicationParameterEnum> communicationParameterEnumIterator = communicationParameterEnumEnumSet.iterator();
        while (communicationParameterEnumIterator.hasNext()) {
            String parameterName = communicationParameterEnumIterator.next().getCommunicationParameter();
            String key = modeName + KEY_SEPARATOR + parameterName;
            if (properties.get(key) != null) {
                configuration.put(key, properties.getProperty(key));
            }
        }
        return configuration;
    }

    // TODO: 2018/1/29 增加配置文件正确性校验 
}
