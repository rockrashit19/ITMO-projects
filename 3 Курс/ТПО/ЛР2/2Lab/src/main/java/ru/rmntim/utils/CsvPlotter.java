package ru.rmntim.utils;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.ChartPanel;

import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.JFrame;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CsvPlotter {
    public static void plot(String csvFilePath,
                            String separator,
                            String chartTitle,
                            String xLabel,
                            String yLabel,
                            double xmin,
                            double xmax) throws IOException {

        XYSeries series = new XYSeries("System Function");

        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(separator);
                if (parts.length >= 2) {
                    try {
                        String rawX = parts[0].replace(',', '.');
                        String rawY = parts[1].replace(',', '.');
                        double x = Double.parseDouble(rawX);
                        double y = Double.parseDouble(rawY);
                        series.add(x, y);
                    } catch (NumberFormatException nfe) {
                        continue;
                    }
                }
            }
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);
        JFreeChart chart = ChartFactory.createXYLineChart(
                chartTitle,
                xLabel,
                yLabel,
                dataset
        );

        XYPlot plot = chart.getXYPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();

        renderer.setSeriesLinesVisible(0, true);
        renderer.setSeriesShapesVisible(0, false);
        plot.setRenderer(renderer);

        NumberAxis yAxis = (NumberAxis) plot.getRangeAxis();
        yAxis.setRange(-100, 100);

        NumberAxis xAxis = (NumberAxis) plot.getDomainAxis();
        xAxis.setRange(xmin, xmax);

        ChartPanel chartPanel = new ChartPanel(chart);
        JFrame frame = new JFrame(chartTitle);
        frame.setContentPane(chartPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
