package cn.seu.edu.LANComm.communication.util;

/**
 * Created by Administrator on 2018/2/24.
 */
public enum ParameterUnitEnum {
    UNIT_HZ("Hz", 1F),
    UNIT_KHZ("kHz", 1_000F),
    UNIT_MHZ("MHz", 1_000_000F),
    UNIT_GHZ("GHz", 1_000_000_000F),
    UNIT_BPS("bps", 1F),
    UNIT_KBPS("kbps", 1_000F),
    UNIT_MBPS("Mbps", 1_000_000F),
    UNIT_GBPS("Gbps", 1_000_000_000F),
    UNIT_DBM("dBm", 1F),
    UNIT_HOPS("Hops", 1F),
    UNIT_KHOPS("kHops", 1_000F);



    private String unit;
    private float value;

    ParameterUnitEnum(String unit, float value) {
        this.unit = unit;
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public float getValue() {
        return value;
    }

}
