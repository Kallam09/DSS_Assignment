package org.example.test;


import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;




public class LineChartPlotter {

  private static final int[] clientCounts = {10, 50, 100};
  private static final int[][] requestRatios = {{2, 1}, {5, 1}, {10, 1}};

  public static void plotLineChart(String title, List<Long> data, int clientCount, int getRequestCount, int postRequestCount) {
    JFrame frame = new JFrame(title);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(800, 600);

    XYSeries series = new XYSeries("Round-trip times");
    for (int i = 0; i < data.size(); i++) {
      series.add(clientCount, data.get(i));
    }

    XYSeriesCollection dataset = new XYSeriesCollection();
    dataset.addSeries(series);

    JFreeChart chart = ChartFactory.createXYLineChart(
        title,
        "Number of clients (GET requests: " + getRequestCount + " POST requests: " + postRequestCount + ")",
        "Round-trip time (ms)",
        dataset
    );

    ChartPanel panel = new ChartPanel(chart);
    frame.getContentPane().add(panel, BorderLayout.CENTER);
    frame.setVisible(true);
  }

  public static void main(String[] args) {
    for (int clientCount : clientCounts) {
      for (int[] requestRatio : requestRatios) {
        int getRequestCount = requestRatio[0];
        int postRequestCount = requestRatio[1];
//        plotLineChart("GET Requests", AudioClientTest.getRoundTripTimes, clientCount, getRequestCount, postRequestCount);
//        plotLineChart("POST Requests", AudioClientTest.postRTTs, clientCount, getRequestCount, postRequestCount);
      }
    }
  }



}



