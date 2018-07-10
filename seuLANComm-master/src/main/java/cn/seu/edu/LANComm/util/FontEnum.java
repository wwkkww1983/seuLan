package cn.seu.edu.LANComm.util;

import com.sun.org.apache.bcel.internal.generic.NEW;
import jdk.nashorn.internal.objects.NativeRangeError;

import java.awt.*;

/**
 * Created by Administrator on 2018/1/26.
 * @author WYCPhoenix
 * @date 2018-1-26-19:22
 */
public enum FontEnum {
    /**
     * 边框字体
     */
    BORDER_TITLE_FONT(new Font("宋体", Font.BOLD, 20)),
    /**
     * 单选按钮字体
     */
     RADIOBUTTOBN_FONT(new Font("宋体", Font.PLAIN, 15)),
    /**
     * 状态栏比较特殊，设置个性的主题字体
     */
    STATUS_NORMAL_FONT(new Font("黑体", Font.BOLD, 15)),
    STATUS_ABNORMAL_FONT(new Font("黑体", Font.BOLD, 15)),
    /**
     * 标签的字体
     */
    LABEL_FONT(new Font("宋体", Font.PLAIN, 15)),
    /**
     * 下拉菜单中元素的字体
     */
    COMBOBOX_ITEM_FONT(new Font("宋体", Font.BOLD, 15)),
    /**
     * 复选框文字的字体
     */
    CHECKBOX_TEXT_FONT(new Font("宋体", Font.PLAIN, 15)),
    /**
     * 文本域字体
     */
    TEXTFIELD_FONT(new Font("宋体", Font.BOLD, 15)),
    /**
     * 按钮字体
     */
    BUTTON_FONT(new Font("宋体", Font.PLAIN, 15)),

    /**
     * chart 标题字体
     */
    CHART_TITLE_FONT(new Font("宋体", Font.BOLD, 10)),
    /**
     * chart XY轴标签字体
     */
    CHART_XYLABEL_FONT(new Font("宋体", Font.PLAIN, 10)),

    PLOT_ITEM_LABEL_FONT(new Font("宋体", Font.PLAIN, 10)),

    PLOT_NO_DATA_MESSAGE_FONT(new Font("宋体", Font.BOLD, 15));

    private Font font;

    private FontEnum(Font font) {
        this.font = font;
    }

    public Font getFont() {
        return font;
    }
}
