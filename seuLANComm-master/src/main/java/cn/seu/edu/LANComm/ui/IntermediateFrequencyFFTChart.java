package cn.seu.edu.LANComm.ui;

import cn.seu.edu.LANComm.communication.util.FramingDecoder;
import cn.seu.edu.LANComm.dsp.Complex;
import cn.seu.edu.LANComm.dsp.FFT;
import cn.seu.edu.LANComm.util.FontEnum;
import jpcap.packet.Packet;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 绘制中频信号的FFT图
 * Created by Administrator on 2018/3/16.
 * @author WYCPhoenix
 */
public class IntermediateFrequencyFFTChart extends ChartPanel implements Runnable{
    /**
     * 绘制FFT时，数据太少没有意义，这里设置数据长度最少为32点
     * 否则，等待下一帧数据，并与前一帧数据合并，直到不小于32点
     */
    private static final int MINIUM_SIZE = 10;
    private static final double EPS = 1E-6;
    private static XYSeries xySeries;
    private long updateIntervalInmills;
    private BlockingQueue<Packet> data;
    private JPanel chartPanel;
    private volatile float sampleRate;
    private volatile boolean isRunning = true;

   public IntermediateFrequencyFFTChart(String chartContent, String chartTitle, String xAxisName,
                                        String yAxisName, double dataLenToShowed, long updateIntervalInmills,
                                        BlockingQueue<Packet> data, JPanel chartPanel) {
       super(createChart(chartContent, chartTitle, xAxisName, yAxisName, dataLenToShowed));
       this.updateIntervalInmills = updateIntervalInmills;
       this.data = data;
       this.chartPanel = chartPanel;
   }

    private static JFreeChart createChart(String chartContent, String chartTitle, String xAxisName,
                                          String yAxisName, double dataLenToShow){
        StandardChartTheme standardChartTheme = new StandardChartTheme("EN");
        standardChartTheme.setExtraLargeFont(FontEnum.CHART_TITLE_FONT.getFont());
        standardChartTheme.setRegularFont(FontEnum.CHART_TITLE_FONT.getFont());
        standardChartTheme.setLargeFont(FontEnum.CHART_TITLE_FONT.getFont());
        ChartFactory.setChartTheme(standardChartTheme);

        xySeries = new XYSeries(chartContent);
        XYSeriesCollection xySeriesCollection = new XYSeriesCollection(xySeries);
        JFreeChart jFreeChart = ChartFactory.createXYLineChart(chartTitle, xAxisName, yAxisName,
                xySeriesCollection, PlotOrientation.VERTICAL, false,true, false);

        ValueAxis valueAxis = jFreeChart.getXYPlot().getDomainAxis();
        valueAxis.setAutoRange(true);
        valueAxis.setFixedAutoRange(dataLenToShow);
        valueAxis.setAutoTickUnitSelection(true);
        valueAxis.setLabelFont(FontEnum.CHART_XYLABEL_FONT.getFont());

        // 设置无数据显示方式
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
                    float[] dataReceived = new FramingDecoder(packet.data).getTransmittedData();
                    // 扩充数据点至不小于原始点数的2的次幂
                    float[] dataToAdd = expandData(dataReceived);
                    // 计算FFT，复数
                    Complex[] fftRes = FFT.fft(convertRealToComplex(dataToAdd));
                    // 计算纵坐标值，实数
                    double[] yValue = getYAxisDataWithLog(fftRes);
                    // 计算横轴坐标，实数
                    double[] xValue = getXAxisDataWithMHzUnit(sampleRate, fftRes.length);
                    // 数值插入
                    for (int index = 0; index <= fftRes.length / 2; index++) {
                        xySeries.addOrUpdate(xValue[index], yValue[index]);
                    }
                    Thread.sleep(this.updateIntervalInmills);
                } else {
                    if (isRunning) {
                        TimedDialog.getDialog("错误", "中频FFT绘制时，接收中频数据超时，" +
                                "请检查接收端数据发送状态", JOptionPane.ERROR_MESSAGE, false, 0);
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return this.chartPanel.getSize();
    }

    /**
     * 将原始数据长度扩充至2的次幂长度，不足使用0填充
     * @param initArray 原始数据向量
     * @return 新的数据向量，长度为2的次幂
     */
    private float[] expandData(float[] initArray) {
        int len = getNextPow2(initArray.length);
        float[] res = new float[len];
        for (int index = 0; index <= len - 1; index++) {
            if (index <= initArray.length - 1) {
                res[index] = initArray[index];
            } else {
                res[index] = 0;
            }
        }
        return res;
    }

    /**
     * 获取不小于 initData 的2的次幂数
     * @param initData 原始数据
     * @return 不小于initData的2的次幂
     */
    private int getNextPow2(int initData) {
        int res = 0;
        if (initData <= MINIUM_SIZE) {
            TimedDialog.getDialog("警告","中频FFT计算时数据量太少", JOptionPane.WARNING_MESSAGE, false,0);
        }
        // 帧长度的限制，数据长度最多255
        if (initData <= 16) {
            res = 16;
        } else if (initData <= 32) {
            res = 32;
        } else if (initData <= 64) {
            res = 64;
        } else if (initData <= 128) {
            res = 128;
        } else if (initData <= 256) {
            res = 256;
        }
        return res;
    }

    /**
     * 计算X轴的数据，单位 MHz
     * @param sampleRate
     * @param Nfft
     * @return
     */
    private double[] getXAxisDataWithMHzUnit(double sampleRate, int Nfft) {
        double[] res = new double[Nfft];
        // 横轴单位使用Hz
        double step = sampleRate / (Nfft);
        for (int index = 0; index < Nfft; index++) {
            res[index] = index * step;
        }
        return res;
    }

    /**
     * 计算功率谱图像的纵轴值，20log10(abs(fft(data)))-maxValue;
     * @param fftRes
     * @return
     */
    private double[] getYAxisDataWithLog(Complex[] fftRes) {
        // step1 求模
        double[] absValue = getComplexValueLength(fftRes);
        // step2 计算log
        double[] temp = new double[fftRes.length];
        for (int index = 0; index <= fftRes.length - 1; index++) {
            temp[index] = 20 * Math.log10(absValue[index]);
            if (temp[index] == Double.NEGATIVE_INFINITY) {
                // -100dB是一个很小的数字了
                temp[index] = -100;
            }
        }
        // step3 归一化
        double maxValue = getMaxValue(temp);
        for (int index = 0; index <= fftRes.length - 1; index++) {
            temp[index] = temp[index] - maxValue;
        }
//        return absValue;
        return temp;

    }

    /**
     * 获取数组中最大的数
     * @param array
     * @return
     */
    private double getMaxValue(double[] array) {
        double max = 0.0;
        for (int index = 0; index <= array.length - 1; index++) {
            if ((array[index] - max) >= EPS) {
                max = array[index];
            }
        }
        return max;
    }

    /**
     * 将一个浮点数数组转为复数数组
     * @param array
     * @return
     */
    private Complex[] convertRealToComplex(float[] array) {
        int len = array.length;
        Complex[] res = new Complex[len];
        for (int index = 0; index <= len - 1; index++) {
            res[index] = new Complex(array[index], 0);
        }
        return res;
    }

    /**
     * 计算复数数组中每个复数的模
     * @param array
     * @return
     */
    private double[] getComplexValueLength(Complex[] array) {
        int len = array.length;
        double[] res = new double[len];
        for (int index = 0; index <= len - 1; index++) {
            res[index] = array[index].abs();
        }
        return res;
    }

    public float getSampleRate() {
        return sampleRate;
    }

    public void setSampleRate(float sampleRate) {
        this.sampleRate = sampleRate;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }
}
