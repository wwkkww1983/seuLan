package cn.seu.edu.LANComm.DynamicChart;

import cn.seu.edu.LANComm.util.FontEnum;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.*;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.TextAnchor;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RectangularShape;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2018/2/1.
 * http://blog.csdn.net/danmo598/article/details/21541177
 * @author WYCPhoenix
 * @date 2018-2-1-22:05
 */
public class CreateTimeSeriesChart extends ChartPanel implements Runnable{

    private long updateIntervalInmills;
    private volatile boolean timeSeriesChartIsRunning = true;
    BlockingQueue<Double> blockingQueue;
    private static TimeSeries timeSeries;
    private JPanel chartPanel;
    /**
     * 创建时序图
     * @param chartTitle 标题
     * @param xAxisName X轴label
     * @param yAxisName Y轴label
     * @param dataLenShowd 展示的数据长度，建议设大一点，以减少CPU压力
     * @param undateIntervalInmills 数据刷新时间，建议设大一点
     */
    public CreateTimeSeriesChart(String chartContent, String chartTitle, String xAxisName,
                                 String yAxisName, double dataLenShowd, long updateIntervalInmills, BlockingQueue<Double> blockingQueue,
                                 JPanel chartPanel) {

        super(createChart(chartContent, chartTitle, xAxisName, yAxisName, dataLenShowd));
        this.updateIntervalInmills = updateIntervalInmills;
        this.blockingQueue = blockingQueue;
        this.chartPanel = chartPanel;
    }

    @SuppressWarnings("all")
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
                yAxisName, timeSeriesCollection, true, true, false);

        // 显示数据点而不显示数据点之间的连线
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) jFreeChart.getXYPlot().getRenderer();
        // 不显示点之间的连线
        renderer.setBaseLinesVisible(false);
        // 显示折点并填充
        renderer.setBaseShapesVisible(true);
        renderer.setBaseShapesFilled(true);
        renderer.setBaseItemLabelsVisible(true);
        // 显示数据点的值
        XYItemRenderer itemRenderer = jFreeChart.getXYPlot().getRenderer();
        itemRenderer.setBaseItemLabelsVisible(true);
        itemRenderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(
                ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_CENTER
        ));
        // 显示Y轴数据
        itemRenderer.setBaseItemLabelGenerator(new StandardXYItemLabelGenerator());
        itemRenderer.setBaseItemLabelFont(FontEnum.PLOT_ITEM_LABEL_FONT.getFont());
        jFreeChart.getXYPlot().setRenderer(itemRenderer);
        //
        NumberAxis numberAxis = (NumberAxis) jFreeChart.getXYPlot().getRangeAxis();
        numberAxis.setAutoTickUnitSelection(true);
        numberAxis.setAutoRangeIncludesZero(true);

        ValueAxis valueAxis = jFreeChart.getXYPlot().getDomainAxis();
        valueAxis.setAutoRange(true);
        valueAxis.setFixedAutoRange(dataLenShowed);
        valueAxis.setLabelFont(FontEnum.CHART_XYLABEL_FONT.getFont());
        return jFreeChart;
    }

    @Override
    public void run() {
        try {
            while (timeSeriesChartIsRunning) {
                Double value = blockingQueue.poll(5000, TimeUnit.MILLISECONDS);
                if (value != null) {
                    timeSeries.addOrUpdate(new Millisecond(), value.doubleValue());
                }
                Thread.sleep(updateIntervalInmills);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension panelSize = this.chartPanel.getSize();
        return panelSize;
    }

}
