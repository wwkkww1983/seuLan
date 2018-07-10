package cn.seu.edu.LANComm.ui;

import cn.seu.edu.LANComm.communication.util.FramingDecoder;
import cn.seu.edu.LANComm.util.FontEnum;
import jpcap.packet.Packet;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import javax.swing.*;
import java.awt.Dimension;
import java.text.SimpleDateFormat;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 绘制跳频图案
 * Created by Administrator on 2018/3/16.
 * @author WYCPhoenix
 */
public class HoppingPatternTimeSeriesChart extends ChartPanel implements Runnable{
    private long updataIntervalInmills;
    private volatile boolean timeSeriesChartIsRunning = true;
    BlockingQueue<Packet> blockingQueue;
    private static TimeSeries timeSeries;
    private JPanel chartPanel;

    public HoppingPatternTimeSeriesChart(String chartContent, String chartTitle, String xAxisName,
                                          String yAxisName, double dataLenToShow, long updataIntervalInmills,
                                          BlockingQueue<Packet> blockingQueue, JPanel chartPanel) {
        super(createChart(chartContent, chartTitle, xAxisName, yAxisName, dataLenToShow));
        this.updataIntervalInmills = updataIntervalInmills;
        this.blockingQueue = blockingQueue;
        this.chartPanel = chartPanel;
    }

    private static JFreeChart createChart(String chartContent, String chartTitle, String xAisName,
                                          String yAxisName, double dataLenToShow) {
        StandardChartTheme standardChartTheme = new StandardChartTheme("EN");
        standardChartTheme.setExtraLargeFont(FontEnum.CHART_TITLE_FONT.getFont());
        standardChartTheme.setRegularFont(FontEnum.CHART_TITLE_FONT.getFont());
        standardChartTheme.setLargeFont(FontEnum.CHART_TITLE_FONT.getFont());
        ChartFactory.setChartTheme(standardChartTheme);

        timeSeries = new TimeSeries(chartContent);
        TimeSeriesCollection timeSeriesCollection = new TimeSeriesCollection(timeSeries);
        JFreeChart jFreeChart = ChartFactory.createTimeSeriesChart(chartTitle, xAisName,
                yAxisName, timeSeriesCollection, false, true, false);
        // 显示数据点而不显示点之间的连线
        XYLineAndShapeRenderer lineAndShapeRenderer = (XYLineAndShapeRenderer) jFreeChart.getXYPlot().getRenderer();
        lineAndShapeRenderer.setBaseLinesVisible(false);
        lineAndShapeRenderer.setBaseShapesVisible(true);
        lineAndShapeRenderer.setBaseShapesFilled(true);
        lineAndShapeRenderer.setBaseItemLabelsVisible(true);

        ValueAxis valueAxis = jFreeChart.getXYPlot().getDomainAxis();
        valueAxis.setAutoRange(true);
        valueAxis.setFixedAutoRange(dataLenToShow);
        valueAxis.setLabelFont(FontEnum.CHART_XYLABEL_FONT.getFont());

        jFreeChart.getXYPlot().setNoDataMessage("No Data");
        jFreeChart.getXYPlot().setNoDataMessageFont(FontEnum.PLOT_NO_DATA_MESSAGE_FONT.getFont());

        // 设置X轴显示方式
        DateAxis dateAxis = (DateAxis) jFreeChart.getXYPlot().getDomainAxis();
        // 时间轴仅显示秒
        SimpleDateFormat format = new SimpleDateFormat("ss");
        dateAxis.setDateFormatOverride(format);

        return jFreeChart;
    }

    @Override
    public void run() {
        try {
            while (timeSeriesChartIsRunning) {
                Packet packet = blockingQueue.poll(20000, TimeUnit.MILLISECONDS);
                if (packet != null) {
                    float[] dataToAdd = new FramingDecoder(packet.data).getTransmittedData();
                    for (float data : dataToAdd) {
                        timeSeries.addOrUpdate(new Millisecond(), data);
                    }
                    Thread.sleep(this.updataIntervalInmills);
                } else {
                    if (timeSeriesChartIsRunning) {
                        TimedDialog.getDialog("错误", "绘制跳频图案时，队列没有数据，请检查发送端数据发送状态", JOptionPane.ERROR_MESSAGE, false, 0);
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean isTimeSeriesChartIsRunning() {
        return timeSeriesChartIsRunning;
    }

    public void setTimeSeriesChartIsRunning(boolean timeSeriesChartIsRunning) {
        this.timeSeriesChartIsRunning = timeSeriesChartIsRunning;
    }

    @Override
    public Dimension getPreferredSize() {
        return this.chartPanel.getSize();
    }
}
