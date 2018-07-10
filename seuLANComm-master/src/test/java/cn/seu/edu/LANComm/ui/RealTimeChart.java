package cn.seu.edu.LANComm.ui;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by Administrator on 2018/2/1.
 */
public class RealTimeChart extends ChartPanel implements Runnable{
    private static TimeSeries timeSeries;

    public RealTimeChart(String chartContent, String title, String yAxisName) {
        super(createChart(chartContent, title, yAxisName));
    }

    private static JFreeChart createChart(String chartContent, String title, String yAxisName) {
        timeSeries = new TimeSeries(chartContent);
        TimeSeriesCollection timeSeriesCollection = new TimeSeriesCollection(timeSeries);
        JFreeChart jFreeChart = ChartFactory.createTimeSeriesChart(title, "时间",
                yAxisName, timeSeriesCollection, true, true, false);
        ValueAxis valueAxis = jFreeChart.getXYPlot().getDomainAxis();
        valueAxis.setAutoRange(true);
        valueAxis.setFixedAutoRange(100000D);
        return jFreeChart;
    }

    public void run() {
        while (true) {
            try {
                timeSeries.add(new Millisecond(), Math.random()*100 - 50);
                Thread.sleep(10);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        StandardChartTheme standardChartTheme = new StandardChartTheme("CN");
        standardChartTheme.setExtraLargeFont(new Font("微软雅黑", Font.BOLD, 20));
        standardChartTheme.setRegularFont(new Font("微软雅黑", Font.PLAIN, 15));
        standardChartTheme.setLargeFont(new Font("微软雅黑", Font.PLAIN, 15));
        ChartFactory.setChartTheme(standardChartTheme);
        JFrame frame = new JFrame("折线图示例");
        RealTimeChart realTimeChart = new RealTimeChart("随机数折线图", "随机数", "数值");
        frame.getContentPane().add(realTimeChart, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
        (new Thread(realTimeChart)).start();
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

}
