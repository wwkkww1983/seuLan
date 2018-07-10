package cn.seu.edu.LANComm.ui;

import cn.seu.edu.LANComm.communication.util.ParameterUnitEnum;
import cn.seu.edu.LANComm.util.CommunicationModeEnum;
import cn.seu.edu.LANComm.util.CommunicationParameterEnum;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 收集所有组件中被选择的值
 * Created by Administrator on 2018/1/27.
 * @author WYCPhoenix
 * @date 2018-1-28-14:58
 */
public final class UIParameterCollector {
    /**
     * 跳频模式标识，结尾必须为-FH
     */
    private static final String FH = "-FH";
    /**
     * field与对应method的映射
     * key 通信参数名
     * value 值对应的setter方法名
     */
    private static  final Map<String, String> methodNameMappingValueSetter = new HashMap<>();
    static {
        methodNameMappingValueSetter.put(CommunicationParameterEnum.PARAMETER_FC.getCommunicationParameter(), "setFc");
        methodNameMappingValueSetter.put(CommunicationParameterEnum.PARAMETER_FH_HOP.getCommunicationParameter(), "setHop");
        methodNameMappingValueSetter.put(CommunicationParameterEnum.PARAMETER_FREQUENCE_OFFSET.getCommunicationParameter(), "setFrequenceOffset");
        methodNameMappingValueSetter.put(CommunicationParameterEnum.PARAMETER_RECEIVE_GAIN.getCommunicationParameter(), "setReceiveGain");
        methodNameMappingValueSetter.put(CommunicationParameterEnum.PARAMETER_TRANSMIT_GAIN.getCommunicationParameter(), "setTransmitGain");
        methodNameMappingValueSetter.put(CommunicationParameterEnum.PARAMETER_RB.getCommunicationParameter(), "setRb");
    }
    /**
     * 增加一个field与对应method的映射关系
     * 便于后序方法调用
     * key 通信参数名
     * value 单位对应的setter方法名
     */
    private static final Map<String, String> methodNameMappingUnitSetter = new HashMap<>();
    static {
        methodNameMappingUnitSetter.put(CommunicationParameterEnum.PARAMETER_FC.getCommunicationParameter(), "setFcUnit");
        methodNameMappingUnitSetter.put(CommunicationParameterEnum.PARAMETER_FH_HOP.getCommunicationParameter(), "setHopUnit");
        methodNameMappingUnitSetter.put(CommunicationParameterEnum.PARAMETER_FREQUENCE_OFFSET.getCommunicationParameter(), "setFrequenceOffsetUnit");
        methodNameMappingUnitSetter.put(CommunicationParameterEnum.PARAMETER_RECEIVE_GAIN.getCommunicationParameter(), "setReceiveGainUnit");
        methodNameMappingUnitSetter.put(CommunicationParameterEnum.PARAMETER_TRANSMIT_GAIN.getCommunicationParameter(), "setTransmitGainUnit");
        methodNameMappingUnitSetter.put(CommunicationParameterEnum.PARAMETER_RB.getCommunicationParameter(), "setRbUnit");
    }
    /**
     * 通信模式
     */
    private String mode;
    /**
     * 码元速率与单位
     */
    private Float rb;
    private String rbUnit;
    /**
     * 载波频率与单位
     */
    private Float fc;
    private String fcUnit;
    /**
     * 发射增益与单位
     */
    private Float transmitGain;
    private String transmitGainUnit;
    /**
     * 接收增益与单位
     */
    private Float receiveGain;
    private String receiveGainUnit;
    /**
     * 信号频偏与单位
     */
    private Float frequenceOffset;
    private String frequenceOffsetUnit;
    /**
     * 跳频速率与单位
     */
    private Float hop;
    private String hopUnit;
    /**
     * 收发模式选择
     */
    private String switchTransmitAndReceive;
    private boolean confirmButtonIsSelected;

    /**
     * MAC地址
     */
    private String TxMAC;
    private String RxMAC;
    private String LocalMAC;

    public UIParameterCollector() {}

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public Float getRb() {
        return rb;
    }

    public void setRb(Float rb) {
        this.rb = rb;
    }

    public String getRbUnit() {
        return rbUnit;
    }

    public void setRbUnit(String rbUnit) {
        this.rbUnit = rbUnit;
    }

    public Float getFc() {
        return fc;
    }

    public void setFc(Float fc) {
        this.fc = fc;
    }

    public String getFcUnit() {
        return fcUnit;
    }

    public void setFcUnit(String fcUnit) {
        this.fcUnit = fcUnit;
    }

    public Float getTransmitGain() {
        return transmitGain;
    }

    public void setTransmitGain(Float transmitGain) {
        this.transmitGain = transmitGain;
    }

    public String getTransmitGainUnit() {
        return transmitGainUnit;
    }

    public void setTransmitGainUnit(String transmitGainUnit) {
        this.transmitGainUnit = transmitGainUnit;
    }

    public Float getReceiveGain() {
        return receiveGain;
    }

    public void setReceiveGain(Float receiveGain) {
        this.receiveGain = receiveGain;
    }

    public String getReceiveGainUnit() {
        return receiveGainUnit;
    }

    public void setReceiveGainUnit(String receiveGainUnit) {
        this.receiveGainUnit = receiveGainUnit;
    }

    public Float getFrequenceOffset() {
        return frequenceOffset;
    }

    public void setFrequenceOffset(Float frequenceOffset) {
        this.frequenceOffset = frequenceOffset;
    }

    public String getFrequenceOffsetUnit() {
        return frequenceOffsetUnit;
    }

    public void setFrequenceOffsetUnit(String frequenceOffsetUnit) {
        this.frequenceOffsetUnit = frequenceOffsetUnit;
    }

    public Float getHop() {
        return hop;
    }

    public void setHop(Float hop) {
        this.hop = hop;
    }

    public String getHopUnit() {
        return hopUnit;
    }

    public void setHopUnit(String hopUnit) {
        this.hopUnit = hopUnit;
    }

    public static Map<String, String> getMethodNameMappingUnitSetter() {
        return methodNameMappingUnitSetter;
    }

    public static Map<String, String> getMethodNameMappingValueSetter() {
        return methodNameMappingValueSetter;
    }

    public String getSwitchTransmitAndReceive() {
        return switchTransmitAndReceive;
    }

    public void setSwitchTransmitAndReceive(String switchTransmitAndReceive) {
        this.switchTransmitAndReceive = switchTransmitAndReceive;
    }

    public boolean isConfirmButtonIsSelected() {
        return confirmButtonIsSelected;
    }

    public void setConfirmButtonIsSelected(boolean confirmButtonIsSelected) {
        this.confirmButtonIsSelected = confirmButtonIsSelected;
    }

    public String getTxMAC() {
        return TxMAC;
    }

    public void setTxMAC(String txMAC) {
        TxMAC = txMAC;
    }

    public String getRxMAC() {
        return RxMAC;
    }

    public void setRxMAC(String rxMAC) {
        RxMAC = rxMAC;
    }

    public String getLocalMAC() {
        return LocalMAC;
    }

    public void setLocalMAC(String localMAC) {
        LocalMAC = localMAC;
    }

    /**
     * 对UIParameterCollector的参数进行汇总
     * key为参数名。value为参数值
     * 注意这里没有对UIParameterCollector参数的合法性进行校验
     * 同时，插入顺序就是以后的发送顺序
     * @return
     */
    public float[] getParameterSelected() {
        List<Float> temp = new ArrayList<>();
        // 通信模式
        String commMode = this.getMode();
        EnumSet<CommunicationModeEnum> modeEnums = EnumSet.allOf(CommunicationModeEnum.class);
        Iterator<CommunicationModeEnum> iterator = modeEnums.iterator();
        while (iterator.hasNext()) {
            CommunicationModeEnum modeEnum = iterator.next();
            if (modeEnum.getCommunicationMode().equals(commMode)) {
                temp.add(new Float(modeEnum.getModeCode()));
                break;
            }
        }
        // 码元速率
        Float Rb = this.getRb();
        String unitRb = this.getRbUnit();
        temp.add(new Float(Rb * getValueByUnit(unitRb)));
        // 载波速率
        Float Fc = this.getFc();
        String unitFc = this.getFcUnit();
        temp.add(new Float(Fc * getValueByUnit(unitFc)));
        // 频偏
        Float frequenceOffset = this.getFrequenceOffset();
        String unitOffset = this.getFrequenceOffsetUnit();
        temp.add(new Float(frequenceOffset * getValueByUnit(unitOffset)));
        // 发送增益
        Float transmitGain = this.getTransmitGain();
        String unitTransmitGain = this.getTransmitGainUnit();
        temp.add(new Float(transmitGain * getValueByUnit(unitTransmitGain)));
        // 接收增益
        Float receiveGain = this.getReceiveGain();
        String unitReceiveGain = this.getReceiveGainUnit();
        temp.add(new Float(receiveGain * getValueByUnit(unitReceiveGain)));
        // 跳变速率
        if (commMode.endsWith(FH)) {
            Float hops = this.getHop();
            String unitHop = this.getHopUnit();
            temp.add(new Float(hops * getValueByUnit(unitHop)));
        }

        //转为待发送数据
        float[] res = new float[temp.size()];
        for (int i = 0; i < temp.size(); i++) {
            res[i] = temp.get(i).floatValue();
        }
        return res;
    }

    private float getValueByUnit(String unit) {
        float res = -1F;
        EnumSet<ParameterUnitEnum> enums = EnumSet.allOf(ParameterUnitEnum.class);
        Iterator<ParameterUnitEnum> iterator = enums.iterator();
        while (iterator.hasNext()) {
            ParameterUnitEnum unitEnum = iterator.next();
            if (unitEnum.getUnit().equals(unit)) {
                res = unitEnum.getValue();
                break;
            }
        }
        return res;
    }

    @Override
    public String toString() {
        return "下位机配置信息{" + "\n" +
                "mode='" + mode + '\'' + "\n" +
                ", rb=" + rb + "\n" +
                ", rbUnit='" + rbUnit + '\'' + "\n" +
                ", fc=" + fc + "\n" +
                ", fcUnit='" + fcUnit + '\'' + "\n" +
                ", transmitGain=" + transmitGain + "\n" +
                ", transmitGainUnit='" + transmitGainUnit + '\'' + "\n" +
                ", receiveGain=" + receiveGain + "\n" +
                ", receiveGainUnit='" + receiveGainUnit + '\'' + "\n" +
                ", frequenceOffset=" + frequenceOffset + "\n" +
                ", frequenceOffsetUnit='" + frequenceOffsetUnit + '\'' + "\n" +
                ", hop=" + hop + "\n" +
                ", hopUnit='" + hopUnit + '\'' + "\n" +
                ", switchTransmitAndReceive='" + switchTransmitAndReceive + '\'' + "\n" +
                ", confirmButtonIsSelected=" + confirmButtonIsSelected + "\n" +
                ", TxMAC='" + TxMAC + '\'' + "\n" +
                ", RxMAC='" + RxMAC + '\'' + "\n" +
                ", LocalMAC='" + LocalMAC + '\'' + "\n" +
                '}'+ "\n";
    }
}
