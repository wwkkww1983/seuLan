package cn.seu.edu.LANComm.ui;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import java.util.Timer;
import java.util.TimerTask;


/**
 * 一个可以定时自动关闭的消息提醒框
 * Created by Administrator on 2018/3/19.
 * @author WYCPhoenix
 */
public class TimedDialog {

    /**
     *
     * @param title 消息框标题
     * @param messae 消息内容
     * @param messgaeType 消息类型
     * @param isModal modal模式
     * @param duration 定时时间，小于等于0则没有定时功能
     */
    public static void getDialog(String title, String messae, int messgaeType, boolean isModal, long duration) {
        JOptionPane optionPane = new JOptionPane(messae);
        optionPane.setMessageType(messgaeType);
        JDialog dialog = optionPane.createDialog(title);
        if (duration > 0L) {
            optionPane.setMessage(messae + "\n将在 " + duration / 1000.0 + " 秒后自动关闭");
            java.util.Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    dialog.setVisible(false);
                    dialog.dispose();
                }
            }, duration);
        } else {
            optionPane.setMessage(messae);
        }
        dialog.setModal(isModal);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);

    }

    public static void main(String[] args) {
        getDialog("testTtile", "testMessage", JOptionPane.INFORMATION_MESSAGE, true, 0);
    }
}
