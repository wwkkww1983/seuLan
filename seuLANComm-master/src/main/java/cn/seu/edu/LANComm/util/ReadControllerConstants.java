package cn.seu.edu.LANComm.util;

import cn.seu.edu.LANComm.ui.TimedDialog;

import javax.swing.JOptionPane;
import java.io.FileInputStream;
import java.io.IOException;

import java.util.Properties;

/**
 * Created by Administrator on 2018/4/20.
 * @author WYCPhoenix
 */
public class ReadControllerConstants {
    private static Properties properties = new Properties();
    static {
        String path = System.getProperty("user.dir").replace('\\', '/') + "/config/" + "PlotTimeControl.properties";
        try(FileInputStream inputStream = new FileInputStream(path)) {
            properties.load(inputStream);
        } catch (IOException e) {
            TimedDialog.getDialog("错误","绘图控制参数文件不存在", JOptionPane.ERROR_MESSAGE, false,0);
        }
    }

    public static String getConstellationDataLenShowed() {
        return properties.getProperty(PlotControllerConstantsEnum.Constellation_DATA_LENGTH_SHOWED.getKey());
    }

    public static String getConstellationUpdateInterval() {
        return properties.getProperty(PlotControllerConstantsEnum.Constellation_UPDATE_INTERVAL.getKey());
    }

    public static String getHoppingPatternDataLenShowed() {
        return properties.getProperty(PlotControllerConstantsEnum.HoppingPattern_DATA_LENGTH_SHOWED.getKey());
    }

    public static String getHoppingPatternUpdateInterval() {
        return properties.getProperty(PlotControllerConstantsEnum.HoppingPattern_UPDATE_INTERVAL.getKey());
    }

    public static String getIntermediateFrequencyFFTDataLenShowed() {
        return properties.getProperty(PlotControllerConstantsEnum.IntermediateFrequencyFFT_DATA_LENGTH_SHOWED.getKey());
    }

    public static String getIntermediateFrequencyFFTUpdateInterval() {
        return properties.getProperty(PlotControllerConstantsEnum.IntermediateFrequencyFFT_UPDATE_INTERVAL.getKey());
    }

    public static String getIntermediateFrequencyDataLenShowed() {
        return properties.getProperty(PlotControllerConstantsEnum.IntermediateFrequency_DATA_LENGTH_SHOWED.getKey());
    }

    public static String getIntermediateFrequencyUpdateInterval() {
        return properties.getProperty(PlotControllerConstantsEnum.IntermediateFrequency_UPDATE_INTERVAL.getKey());
    }
}
