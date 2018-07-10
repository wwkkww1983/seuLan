package cn.seu.edu.LANComm.ui;

import cn.seu.edu.LANComm.util.ReadControllerConstants;
import jpcap.packet.Packet;

import javax.swing.JPanel;
import java.awt.Color;
import java.util.concurrent.BlockingQueue;

/**
 * 在这里绘制跳频图案数据
 * Created by Administrator on 2018/1/27.
 */
public class PlotHoppingPatternPart implements Runnable{
    private static final String CHART_CONTENT = "跳频图案";
    private static final String CHART_TITLE = "跳频图案时序图";
    private static final String XLABEL_NAME = "时间";
    private static final String YLABEL_NAME = "频率";
    private static final double DATA_LENGTH_SHOWED = Double.parseDouble(ReadControllerConstants.getHoppingPatternDataLenShowed());
    private static final long UPDATE_INTERVAL = Long.parseLong(ReadControllerConstants.getHoppingPatternUpdateInterval());
    private  HoppingPatternTimeSeriesChart hoppingPatternTimeSeriesChart;
    private JPanel chartPanel;
    private volatile boolean isRunning = true;

    public PlotHoppingPatternPart(JPanel chartPanel) {
        this.chartPanel = chartPanel;
    }

    public JPanel createHoppingPatternChart(BlockingQueue<Packet> dataToShow) {
        hoppingPatternTimeSeriesChart = new HoppingPatternTimeSeriesChart(
                CHART_CONTENT, CHART_TITLE, XLABEL_NAME, YLABEL_NAME, DATA_LENGTH_SHOWED, UPDATE_INTERVAL, dataToShow, chartPanel);

        chartPanel.setBackground(Color.WHITE);
        chartPanel.add(hoppingPatternTimeSeriesChart);

        return chartPanel;
    }

    @Override
    public void run() {
        new Thread(hoppingPatternTimeSeriesChart).start();
    }

    public void stopThread() {
        hoppingPatternTimeSeriesChart.setTimeSeriesChartIsRunning(false);
    }

    public void startThread() {
        hoppingPatternTimeSeriesChart.setTimeSeriesChartIsRunning(true);
    }
}
