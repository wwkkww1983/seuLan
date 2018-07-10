package cn.seu.edu.LANComm.ui;

import com.sun.org.apache.bcel.internal.generic.NEW;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.Timer;

/**
 * Created by Administrator on 2018/3/16.
 */
public class H extends JDialog{
    /**
     * 窗体标题
     */
    private String title;
    /**
     * 显示的消息
     */
    private String message;
    /**
     * 窗体大小
     */
    private Dimension size;
    /**
     * 父对象
     */
    private Frame parentFrame;
    /**
     * 是否为modal模式
     */
    private boolean isModal;
    /**
     * 自动关闭时间
     */
    private long duration;

    public H(String title, String message, Dimension size, Frame parentFrame, boolean isModal, long duration) {
        this.title = title;
        this.message = message;
        this.size = size;
        this.parentFrame = parentFrame;
        this.isModal = isModal;
        this.duration = duration;
    }

    public void getDialog() {
        JDialog dialog = new JDialog(this.parentFrame, this.title, this.isModal);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
        dialog.setSize(this.size);
        dialog.setLocationRelativeTo(null);
        dialog.setLayout(new GridLayout(2,1));
        /**
         * 大于0说明需要定时关闭
         */
        if (duration > 0) {
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    dialog.setVisible(false);
                    dialog.dispose();
                }
            }, duration);
            dialog.add(new JLabel(message + "\n将在 " + duration / 1000 + " 秒后自动关闭"));
        } else {
            dialog.add(new JLabel(message));
        }
        JButton button = new JButton("确认");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
        JLabel dump1 = new JLabel();
        JLabel dump2 = new JLabel();
        JPanel panel = new JPanel(new GridLayout(1,3));
        panel.add(dump1);
        panel.add(button);
        panel.add(dump2);
        dialog.add(panel);
        dialog.pack();
    }

    public static void main(String[] args) {
        String title = "testTitle";
        String message = "testMessage";
        Dimension size = new Dimension(100,200);
        Frame parentFrame = null;
        boolean modal = true;
        long duration = 5000;

        H h = new H(title, message, size, parentFrame, modal, duration);
        h.getDialog();
    }

}
