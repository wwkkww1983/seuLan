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
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2018/2/1.
 * @author WYCPhoenix
 * @date 2018-2-1-22:05
 */
public class CreateTimeSeriesChart extends ChartPanel implements Runnable{

    private static TimeSeries timeSeries;
    private long undateIntervalInmills;
    private BlockingQueue<Packet> data;
    private JPanel chartPanel;
    private volatile boolean isRunning = true;
    /**
     * 创建时序图
     * @param chartContent legend
     * @param chartTitle 标题
     * @param xAxisName X轴label
     * @param yAxisName Y轴label
     * @param dataLenShowd 展示的数据长度，建议设大一点，以减少CPU压力
     * @param undateIntervalInmills 数据刷新时间，建议设大一点
     */
    public CreateTimeSeriesChart(String chartContent, String chartTitle, String xAxisName,
                                 String yAxisName, double dataLenShowd, long undateIntervalInmills,
                                 BlockingQueue<Packet> data, JPanel chartPanel) {
        super(createChart(chartContent, chartTitle, xAxisName, yAxisName, dataLenShowd));
        this.undateIntervalInmills = undateIntervalInmills;
        this.data = data;
        this.chartPanel = chartPanel;
    }

    private static JFreeChart createChart(String chartContent, String chartTitle, String xAxisName,
                                          String yAxisName, double dataLenShowed) {

        StandardChartTheme standardChartTheme = new StandardChartTheme("EN");
        standardChartTheme.setExtraLargeFont(FontEnum.CHART_TITLE_FONT.getFont());
        standardChartTheme.setRegularFont(FontEnum.CHART_TITLE_FONT.getFont());
        standardChartTheme.setLargeFont(FontEnum.CHART_TITLE_FONT.getFont());
        ChartFactory.setChartTheme(standardChartTheme);

        timeSeries = new TimeSeries(chartContent);
        TimeSeriesCollection timeSeriesCollection = new TimeSeriesCollection(timeSeries);
        JFreeChart jFreeChart = ChartFactory.createTimeSeriesChart(chartTitle, xAxisName,
                yAxisName, timeSeriesCollection, false, true, false);

        ValueAxis valueAxis = jFreeChart.getXYPlot().getDomainAxis();
        valueAxis.setAutoRange(true);
        valueAxis.setFixedAutoRange(dataLenShowed);
        valueAxis.setLabelFont(FontEnum.CHART_XYLABEL_FONT.getFont());

        // 设置X轴显示方式
        DateAxis dateAxis = (DateAxis) jFreeChart.getXYPlot().getDomainAxis();
        // 时间轴仅显示秒
        SimpleDateFormat format = new SimpleDateFormat("ss");
        dateAxis.setDateFormatOverride(format);
        // 设置无数据时显示
        jFreeChart.getXYPlot().setNoDataMessage("No Data");
        jFreeChart.getXYPlot().setNoDataMessageFont(FontEnum.PLOT_NO_DATA_MESSAGE_FONT.getFont());

        return jFreeChart;

    }
    @Override
    public void run() {
        try {
            while (isRunning) {
                Packet packet = data.poll(10000, TimeUnit.MILLISECONDS);
                if (packet != null) {
                    float[] dataToAdd = new FramingDecoder(packet.data).getTransmittedData();
                    for (float data : dataToAdd) {
                        timeSeries.addOrUpdate(new Millisecond(), data);
                    }
                    Thread.sleep(this.undateIntervalInmills);
                } else {
                    if (isRunning) {
                        TimedDialog.getDialog("错误", "绘制中频信号时，中频信号缓冲区为空，请检查发送端数据发送状态", JOptionPane.ERROR_MESSAGE, false, 0);
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
     * 重写ChartPanel的getPreferredSize
     * 实现画布大小动态调整
     * @return
     */
    @Override
    public Dimension getPreferredSize() {

        return chartPanel.getSize();
    }

    public BlockingQueue<Packet> getData() {
        return data;
    }

    public void setData(BlockingQueue<Packet> data) {
        this.data = data;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();

        String chartContent = "随机数";
        String chartTitle = "随机数标题";
        String xAxisLabel = "时间";
        String yAxisLabel = "随机数";
        double dataLenShowed = 10000d;
        long updateInterval = 100;
        BlockingQueue<Packet> data = null;

        CreateTimeSeriesChart createTimeSeriesChart = new CreateTimeSeriesChart(
                chartContent, chartTitle, xAxisLabel, yAxisLabel,dataLenShowed, updateInterval, data, new JPanel());
        frame.getContentPane().add(createTimeSeriesChart);
        frame.pack();
        frame.setVisible(true);
        (new Thread(createTimeSeriesChart)).start();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
