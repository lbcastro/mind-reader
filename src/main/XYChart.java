package main;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYTitleAnnotation;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.RectangleInsets;

@SuppressWarnings("serial")
public class XYChart extends JPanel {
	
	
	// Initiate objects.
	private 			ChartPanel			chartPanel;
	private 			ValueAxis 			domainAxis;
	private 			ValueAxis 			rangeAxis;
	
	private static 	XYPlot 				chartPlot;
	private static 	XYSeries 			xySeries;
	private static 	XYSeriesCollection 	dataSet;
	//
	
	
	// Class methods.
	public static XYPlot getPlot() 
	{
		return chartPlot;
	}
	
	public static XYSeries getSeries() 
	{
		return xySeries;
	}
	
	public void setPanelSize(int x, int y) 
	{
		chartPanel.setPreferredSize(new Dimension(x,y));
	}

	public void setDomainLabel(String label) 
	{ 
		domainAxis.setLabel(label); 
	}
	
	public void setDomainRange(double min, double max, boolean fixed)
	{
		if (fixed) domainAxis.setFixedAutoRange(max-min);
		
		else 
		{ 
			domainAxis.setRange(min, max);
			domainAxis.setLowerBound(min);
			domainAxis.setUpperBound(max);
		}
	}

	public void setRangeRange(double x, double y, boolean fixed)
	{
		if (fixed) rangeAxis.setFixedAutoRange(y-x);
		else rangeAxis.setRange(x, y);
	}

	public void setRangeLabel(String label) 
	{
		rangeAxis.setLabel(label);
	}
	
	public void setRangeVisible(boolean visible) 
	{
		rangeAxis.setVisible(visible);
	}
	
	public void setRangeCenter(double center) 
	{
		rangeAxis.centerRange(center);
	}

	public void setChartColor(Color color) 
	{
		getPlot().setBackgroundPaint(color);
	}

	public void setLinesColor(Color color) 
	{
		getPlot().setRangeGridlinePaint(color);
		getPlot().setDomainGridlinePaint(color);
	}
	
	public static XYSeriesCollection getDataSet() 
	{
		return dataSet;
	}

	public void setDataSet(XYSeriesCollection dataSet) 
	{
		XYChart.dataSet = dataSet;
	}
	
	public void createDataSet() {}
	
	public void defineChart(XYPlot plot) {}
	//
	
	
	// Chart creation. 
	XYChart() {
		
		
		// Initiate the dataset.
		createDataSet();
		//
		
		
		// Chart's properties.
		JFreeChart chart = ChartFactory.createXYLineChart(
				"", 
				"Chart", 
				"", 
				dataSet, 
				PlotOrientation.VERTICAL, 
				false, 
				true, 
				false
				);
		
		chart.setAntiAlias(true);
		chart.setBorderPaint(Color.LIGHT_GRAY);
		chart.setBorderVisible(true);
		//
		
		
		// Axis' properties.
		chartPlot = chart.getXYPlot();
		defineChart(chartPlot);
		
		domainAxis = chartPlot.getDomainAxis();
		domainAxis.setAxisLineVisible(false);

		rangeAxis = chartPlot.getRangeAxis();
		rangeAxis.setTickMarksVisible(false);
		rangeAxis.setAxisLineVisible(false);
		rangeAxis.setVisible(false);
		
		chartPlot.setInsets(new RectangleInsets(5, 10, 5, 10));
		//
		
		
		// Legend.
		LegendTitle legend = new LegendTitle(getPlot());
		legend.setItemPaint(Color.WHITE);

		XYTitleAnnotation annotation = new XYTitleAnnotation(1, 1, legend, RectangleAnchor.TOP_RIGHT);
		chartPlot.addAnnotation(annotation);
		//
		
		
		// Renderer.
		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		
		renderer.setSeriesPaint(0, Color.decode("#286CB5"));
		renderer.setSeriesLinesVisible(0, true);
		renderer.setSeriesShapesVisible(0, false);
		
		chartPlot.setRenderer(renderer);
		//
		
		
		// Panel.
		chartPanel = new ChartPanel(chart);
		add(chartPanel);
		//
	}
}
