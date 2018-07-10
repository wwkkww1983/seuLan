package cn.seu.edu.LANComm.ui;

import javax.swing.*;
import java.util.*;
import java.util.Timer;

/**
 * Created by Administrator on 2018/3/19.
 */

public class TimeDialog {


    public void getDialog(String title, String messae, int messgaeType, boolean isModal, long duration) {
        JOptionPane optionPane = new JOptionPane(messae);
        optionPane.setMessageType(messgaeType);
        JDialog dialog = optionPane.createDialog(title);
        if (duration > 0L) {
            optionPane.setMessage(messae + "\n将在 " + duration / 1000 + " 秒自动关闭");
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
        TimeDialog dialog = new TimeDialog();
        dialog.getDialog("titleTest", "messageTest", JOptionPane.INFORMATION_MESSAGE, true, 2000);
    }

}
