package cn.seu.edu.LANComm.DynamicChart;


import org.jfree.chart.ChartPanel;
import org.jfree.data.time.TimeSeries;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2018/2/24.
 */
public class ProdecuData2 implements Runnable{
    private BlockingQueue<Double> data;
    private volatile boolean isRunning = true;

    public ProdecuData2(BlockingQueue<Double> data, boolean isRunning) {
        this.data = data;
        this.isRunning = isRunning;
    }


    @Override
    public void run() {
        try{
            while (isRunning) {
                data.offer(Math.random(), 5000, TimeUnit.MILLISECONDS);
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public BlockingQueue<Double> getData() {
        return data;
    }

    public void setData(BlockingQueue<Double> data) {
        this.data = data;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }


    @SuppressWarnings("all")
    public static void main(String[] args) {
        BlockingQueue<Double> data = new LinkedBlockingQueue<>();
        ProdecuData2 prodecuData2 = new ProdecuData2(data, true);

        String chartContent = "随机数";
        String chartTitle = "随机数标题";
        String xAxisLabel = "时间";
        String yAxisLabel = "随机数xxx";
        double dataLenShowed = 10000d;
        long updateInterval = 100;

        JPanel panel = new JPanel();
        CreateTimeSeriesChart timeSeriesChart = new CreateTimeSeriesChart(chartContent, chartTitle, xAxisLabel, yAxisLabel,
                dataLenShowed, updateInterval,data, panel);
//        timeSeriesChart.setMaximumSize(new Dimension(100, 200));
//        timeSeriesChart.setMinimumDrawWidth(100);
//        timeSeriesChart.setMinimumDrawHeight(400);
//        timeSeriesChart.setMaximumDrawHeight(400);
//        timeSeriesChart.setMaximumDrawWidth(400);
//        timeSeriesChart.setMinimumSize(new Dimension(100, 300));
//        timeSeriesChart.setMaximumSize(new Dimension(400, 400));
        JFrame frame = new JFrame();
//        frame.getContentPane().add(timeSeriesChart);

        panel.add(timeSeriesChart);
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        new Thread(prodecuData2).start();
        new Thread(timeSeriesChart).start();

//        try {
//            Thread.sleep(5000);
//            prodecuData2.isRunning = false;
//            System.out.println("生产线程结束");
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }



    }

}
