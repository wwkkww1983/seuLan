package cn.seu.edu.LANComm.ui;

import cn.seu.edu.LANComm.util.FontEnum;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/27.
 */
public class CommunicationTXRxSelectorPart {
    /**
     * 主panel网格布局参数
     */
    private static final int DEFAULT_GRID_ROWS = 6;
    private static final int DEFAULT_GRID_COLUMN = 1;
    /**
     * 收发文本显示
     */
    private static final String TRANSMIT_TEXT = "发送";
    private static final String RECEIVE_TEXT = "接收";
    /**
     * 主 panel 默认大小
     */
    private static final int DEFAULT_PANEL_WIDTH = 150;
    private static final int DEFAULT_PANEL_HEIGHT = 300;

    private static final String STATUS_PANEL = "StatusPanel";
    private static final String CONFIRM_BUTTON = "ConfirmButton";


    public static Map<String, Object> createCommunicationTXRxSelectorPanel(UIParameterCollector collector) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(DEFAULT_GRID_ROWS,DEFAULT_GRID_COLUMN));

        ButtonGroup buttonGroup = new ButtonGroup();
        // 默认选择发送模式
        JRadioButton transmit = new JRadioButton(TRANSMIT_TEXT, true);
        collector.setSwitchTransmitAndReceive(TRANSMIT_TEXT);
        transmit.setBackground(Color.WHITE);
        transmit.setFont(FontEnum.RADIOBUTTOBN_FONT.getFont());
        transmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                collector.setSwitchTransmitAndReceive(TRANSMIT_TEXT);
            }
        });
        buttonGroup.add(transmit);
        JPanel trasmitPanel = new JPanel();
        trasmitPanel.setBackground(Color.WHITE);
        trasmitPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        trasmitPanel.add(transmit);

        JRadioButton receive = new JRadioButton(RECEIVE_TEXT);
        receive.setFont(FontEnum.RADIOBUTTOBN_FONT.getFont());
        receive.setBackground(Color.WHITE);
        receive.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                collector.setSwitchTransmitAndReceive(RECEIVE_TEXT);
            }
        });
        buttonGroup.add(receive);
        JPanel receivePanel = new JPanel();
        receivePanel.setBackground(Color.WHITE);
        receivePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        receivePanel.add(receive);

        JPanel confirmPanel = new JPanel();
        JButton confirm = new JButton("确认");
        confirm.setFont(FontEnum.BUTTON_FONT.getFont());
        collector.setConfirmButtonIsSelected(false);
        confirm.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                collector.setConfirmButtonIsSelected(true);
            }
        });
        confirmPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        confirmPanel.setBackground(Color.WHITE);
        confirmPanel.add(confirm);

        panel.add(trasmitPanel);
        panel.add(receivePanel);
        panel.add(confirmPanel);

        Border titledBorder = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK),
                                            "收发确认", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
                                            FontEnum.BORDER_TITLE_FONT.getFont());
        panel.setBorder(titledBorder);
        panel.setPreferredSize(new Dimension(DEFAULT_PANEL_WIDTH, DEFAULT_PANEL_HEIGHT));
        panel.setBackground(Color.WHITE);
        Map<String, Object> map = new HashMap<>();
        map.put(STATUS_PANEL, panel);
        map.put(CONFIRM_BUTTON, confirm);
        return map;
    }

    public static String getStatusPanel() {
        return STATUS_PANEL;
    }

    public static String getConfirmButton() {
        return CONFIRM_BUTTON;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        UIParameterCollector collector = new UIParameterCollector();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Map<String, Object> map = createCommunicationTXRxSelectorPanel(collector);
        frame.add((JPanel)map.get(STATUS_PANEL));
        frame.pack();
        frame.setVisible(true);
    }
}
