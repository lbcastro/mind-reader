package main;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RectangleInsets;

@SuppressWarnings("serial")
public class BarChart extends JPanel {


	// Initiate required objects.
	private 		 ChartPanel chartPanel;
	private 		 CategoryPlot chartPlot;
	private static DefaultCategoryDataset dataSet;
	//


	// Chart methods.
	public void setChartColor(Color color) 
	{
		chartPlot.setBackgroundPaint(color);
	}
	
	public void setLinesColor(Color color)
	{
		chartPlot.setRangeGridlinePaint(color);
		chartPlot.setDomainGridlinePaint(color);
	}
	
	public static void addValue(String band, double value)
	{
		dataSet.addValue(value, "", band);
	}
	//
	
	
	// Create the chart.
	public BarChart() {

		
		//
		dataSet = new DefaultCategoryDataset();
		BarRenderer.setDefaultShadowsVisible(false);
		//
		
		
		// Chart's properties.
		JFreeChart chart = ChartFactory.createBarChart(
				"", 
				"", 
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
		chartPlot = chart.getCategoryPlot();
		//
		
		
		// Axis properties.
		final ValueAxis rangeAxis = chartPlot.getRangeAxis();
		rangeAxis.setVisible(false);
		rangeAxis.setRange(0.0, 100.0);
		
		final CategoryAxis domainAxis = chartPlot.getDomainAxis();
		domainAxis.setLabel("Bands");
		domainAxis.setLabelFont(new Font("Dialog", Font.BOLD, 14));
		domainAxis.setAxisLineVisible(false);
		domainAxis.setLabelInsets(new RectangleInsets(-1,0,0,0));
		//
		
		
		// Customization.
		((BarRenderer) chartPlot.getRenderer()).setBarPainter(new StandardBarPainter());
		BarRenderer renderer = (BarRenderer) chartPlot.getRenderer();
		renderer.setDrawBarOutline(false);
		renderer.setSeriesPaint(0, Color.decode("#286CB5"));
		
		chartPlot.setInsets(new RectangleInsets(5, 11, 7, 10));
		//

		
		// Panel.
		chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(290,250));
		
		add(chartPanel);
		//
	}
}
