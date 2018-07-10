package cn.seu.edu.LANComm.util;

/**
 * Created by Administrator on 2018/4/20.
 */
public enum PlotControllerConstantsEnum {
    Constellation_DATA_LENGTH_SHOWED("ConstellationDiagram_DATA_LENGTH_SHOWED", "星座图绘制长度"),
    Constellation_UPDATE_INTERVAL("ConstellationDiagram_UPDATE_INTERVAL", "星座图刷新间隔"),
    HoppingPattern_DATA_LENGTH_SHOWED("PlotHoppingPattern_DATA_LENGTH_SHOWED", "跳频图案绘制长度"),
    HoppingPattern_UPDATE_INTERVAL("PlotHoppingPattern_UPDATE_INTERVAL", "跳频图案刷新间隔"),
    IntermediateFrequencyFFT_DATA_LENGTH_SHOWED("PlotIntermediateFrequencyFFT_DATA_LENGTH_SHOWED", "中频信号FFT绘制长度"),
    IntermediateFrequencyFFT_UPDATE_INTERVAL("PlotIntermediateFrequencyFFT_UPDATE_INTERVAL", "中频信号FFT刷新间隔"),
    IntermediateFrequency_DATA_LENGTH_SHOWED("PlotIntermediateFrequency_DATA_LENGTH_SHOWED", "中频信号时域图绘制长度"),
    IntermediateFrequency_UPDATE_INTERVAL("PlotIntermediateFrequency_UPDATE_INTERVAL", "中频信号时域图刷新时间");


    private String key;
    private String desription;
    PlotControllerConstantsEnum(String key, String description) {
        this.key = key;
        this.desription = description;
    }

    public String getKey() {
        return key;
    }

    public String getDesription() {
        return desription;
    }
}
