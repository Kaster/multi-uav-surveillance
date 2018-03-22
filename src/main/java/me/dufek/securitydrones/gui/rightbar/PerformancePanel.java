package me.dufek.securitydrones.gui.rightbar;

import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.BorderFactory;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.time.MovingAverage;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

/**
 *
 * @author Jan Dufek
 */
public class PerformancePanel implements rightBarItem {

    private final Container pane;

    private XYDataset dataset;
    private JFreeChart chart;
    private ChartPanel chartPanel;
    private XYItemRenderer renderer;
    private StandardXYToolTipGenerator toolTipGenerator;
    private TimeSeries heat;

    public PerformancePanel(Container pane) {
        this.pane = pane;
    }

    private XYDataset createDataset() {
        heat = new TimeSeries("Heat");
        final TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(heat);
        return dataset;
    }

    private JFreeChart createChart(final XYDataset dataset) {
        chart = ChartFactory.createTimeSeriesChart(
                null,
                null,
                null,
                dataset,
                false,
                false,
                false
        );

        renderer = chart.getXYPlot().getRenderer();

        toolTipGenerator = new StandardXYToolTipGenerator(
                StandardXYToolTipGenerator.DEFAULT_TOOL_TIP_FORMAT,
                new SimpleDateFormat("s"), new DecimalFormat("0.00")
        );

        renderer.setToolTipGenerator(toolTipGenerator);

        return chart;
    }

    @Override
    public void create() {
        dataset = createDataset();
        chart = createChart(dataset);
        chartPanel = new ChartPanel(chart);
        chartPanel.setMouseZoomable(true, false);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 0.4;
        constraints.weighty = 0.4;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        chartPanel.setBorder(BorderFactory.createTitledBorder("Area Coverage (Average Cell Review Time)"));
        
        pane.add(chartPanel, constraints);
    }

    @Override
    public void setText(String text) {
        throw new IllegalStateException("Set text is not supported for graph.");
    }

    public void addMeasurement(Date time, double value) {
        heat.addOrUpdate(new Second(time), value);
    }
}
