package cn.seu.edu.LANComm.ui;

import cn.seu.edu.LANComm.communication.util.FramingDecoder;
import cn.seu.edu.LANComm.util.FontEnum;
import jpcap.packet.Packet;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.Dimension;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 星座图绘图
 * Created by Administrator on 2018/3/16.
 * @author WYCPhoenix
 */
public class ConstellationDiagramChart extends ChartPanel implements Runnable {
    private static XYSeries xySeries;
    private long updateIntervalInmills;
    private BlockingQueue<Packet> data;
    private JPanel chartPanel;
    private volatile boolean isRunning = true;

    public ConstellationDiagramChart(String chartContent, String chartTitle, String xAxisName,
                                     String yAxisName, double dataLenToShowed, long updateIntervalInmills,
                                     BlockingQueue<Packet> data, JPanel chartPanel) {
        super(createChart(chartContent, chartTitle, xAxisName, yAxisName, dataLenToShowed));
        this.updateIntervalInmills = updateIntervalInmills;
        this.data = data;
        this.chartPanel = chartPanel;
    }

    private static JFreeChart createChart(String chartContent, String chartTitle, String xAxisLabel,
                                String yAxisName, double dataLenToShow) {
        StandardChartTheme standardChartTheme = new StandardChartTheme("EN");
        standardChartTheme.setExtraLargeFont(FontEnum.CHART_TITLE_FONT.getFont());
        standardChartTheme.setRegularFont(FontEnum.CHART_TITLE_FONT.getFont());
        standardChartTheme.setLargeFont(FontEnum.CHART_TITLE_FONT.getFont());
        ChartFactory.setChartTheme(standardChartTheme);

        xySeries = new XYSeries(chartContent);
        XYSeriesCollection xySeriesCollection = new XYSeriesCollection(xySeries);
        JFreeChart jFreeChart = ChartFactory.createXYLineChart(chartTitle, xAxisLabel,yAxisName,
                                    xySeriesCollection, PlotOrientation.VERTICAL, false, true, false);
        ValueAxis valueAxis = jFreeChart.getXYPlot().getDomainAxis();
        valueAxis.setAutoRange(true);
        valueAxis.setFixedAutoRange(dataLenToShow);
        valueAxis.setLabelFont(FontEnum.CHART_XYLABEL_FONT.getFont());

        // 显示数据点而不显示点之间的连线
        XYLineAndShapeRenderer lineAndShapeRenderer = (XYLineAndShapeRenderer) jFreeChart.getXYPlot().getRenderer();
        lineAndShapeRenderer.setBaseLinesVisible(false);
        lineAndShapeRenderer.setBaseShapesFilled(true);
        lineAndShapeRenderer.setBaseShapesVisible(true);

        // 设置无数据显示方式
        jFreeChart.getXYPlot().setNoDataMessageFont(FontEnum.PLOT_NO_DATA_MESSAGE_FONT.getFont());
        jFreeChart.getXYPlot().setNoDataMessage("No Data");

        return jFreeChart;
    }

    @Override
    public void run() {
        try {
            while (isRunning) {
                Packet packet = data.poll(10000, TimeUnit.MILLISECONDS);
                if (packet != null) {
                    float[] dataToAdd = new FramingDecoder(packet.data).getTransmittedData();
                    if (dataToAdd.length % 2 != 0) {
                        TimedDialog.getDialog("错误","星座数据IQ必须成对出现，该帧将被忽略", JOptionPane.ERROR_MESSAGE, false,0);
                    } else {
                        for (int index = 0; index < dataToAdd.length; ) {
                            xySeries.addOrUpdate(dataToAdd[index], dataToAdd[++index]);
                            index++;
                        }
                        Thread.sleep(this.updateIntervalInmills);
                    }

                } else {
                    if (isRunning) {
                        TimedDialog.getDialog("错误", "绘制星座图时星座数据队列为空，请检查接收端数据发送状态", JOptionPane.ERROR_MESSAGE, false, 0);
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    @Override
    public Dimension getPreferredSize() {
        return this.chartPanel.getSize();
    }
}
