package cn.seu.edu.LANComm.util;

/**
 * Created by Administrator on 2018/1/26.
 * @author WYCPhoenix
 * @date 2018-1-26-19:22
 */
public enum CommunicationParameterEnum {
    PARAMETER_RB("Rb","码元速率"),
    PARAMETER_FC("Fc", "载波频率"),
    PARAMETER_TRANSMIT_GAIN("Transmit-Gain", "发射信号增益"),
    PARAMETER_RECEIVE_GAIN("Receive-Gain", "接收信号增益"),
    PARAMETER_FREQUENCE_OFFSET("Frequence-Offset", "信号频偏"),
    PARAMETER_FH_HOP("Hop", "跳频速率");

    /**
     * communicationParameter 参数名
     */
    private String communicationParameter;
    /**
     * description 参数提示
     */
    private String description;
    CommunicationParameterEnum(String communicationParameter, String description) {
        this.communicationParameter = communicationParameter;
        this.description = description;
    }

    public String getCommunicationParameter() {
        return communicationParameter;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "CommunicationParameterEnum{" +
                "communicationParameter='" + communicationParameter + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
