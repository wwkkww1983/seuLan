package cn.seu.edu.LANComm.util;

/**
 * Created by Administrator on 2018/1/26.
 * @author WYCPhoenix
 * @date 2018-1-26-18:00
 *
 * 支持的通信类型枚举
 */
public enum CommunicationModeEnum {
    /**
     * DQPSK模式
     */
    DQOSK_MODE("DQPSK", 1, "DPQSK调制模式"),
    /**
     * DQPSK-DSSS模式
     */
    DQOSK_DSSS_MODE("DQPSK-DSSS", 2, "DPQSK-DSSS模式"),
    /**
     * DQPSK-FH模式
     */
    DQOSK_FH_MODE("DQPSK-FH", 3, "DQPSK-FH模式");
    private String communicationMode;
    private int modeCode;
    private String description;

    CommunicationModeEnum(String communicationMode, int modeCode, String description) {
        this.communicationMode = communicationMode;
        this.modeCode = modeCode;
        this.description = description;
    }

    public String getCommunicationMode() {
        return communicationMode;
    }

    public int getModeCode() {
        return modeCode;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "CommunicationModeEnum{" +
                "communicationMode='" + communicationMode + '\'' +
                ", modeCode=" + modeCode +
                ", description='" + description + '\'' +
                '}';
    }
}
