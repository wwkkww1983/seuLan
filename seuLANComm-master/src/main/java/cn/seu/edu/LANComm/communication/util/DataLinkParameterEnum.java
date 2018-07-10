package cn.seu.edu.LANComm.communication.util;

/**
 * 数据链路层帧枚举
 * Created by Administrator on 2018/2/4.
 */
public enum DataLinkParameterEnum {
    /**
     * 帧类型
     */
    FRAME_TYPE("8511", "帧类型FrameType"),
    /**
     * 参数识别符
     */
    TEST("0", "连接测试"),
    PARAMETER_SETTING("1", "通信参数配置数据，float类型"),
    CONSTELLATION_DATA("2", "星座图数据数据，float类型"),
    INTERMEDIATE_DATA("3", "中频时域信号数据，float类型"),
    HOPPING_PATTERN_DATA("4", "跳频图案数据，float类型"),
    TRANSMITTED_SYMBOL_DATA("5", "发送的符号，float类型"),
    RECEIVED_SYMBOL_DATA("6", "接收的符号，float类型"),
    SAMPLE_RATE("7", "中频采样率，float类型"),
    COMMUNICATION_START("8", "前置工作完成，通信开始， float类型"),
    COMMUNICATION_STOP("9", "通信结束，停止数据发送");

    /**
     * 参数类型
     */
    private String dataType;
    /**
     * 参数描述
     */
    private String description;

    private DataLinkParameterEnum(String dataType, String description) {
        this.dataType = dataType;
        this.description = description;
    }

    public String getDataType() {
        return dataType;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "DataLinkParameterEnum{" +
                "dataType='" + dataType + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
