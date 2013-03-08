package main;

import java.awt.Color;
import java.text.DecimalFormat;

import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.IntervalMarker;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;


@SuppressWarnings("serial")
public class EegChart extends XYChart {
	
	
	// Define initial objects.
	private static XYSeries eegSeries;
	private static XYPlot 	  eegPlot;
	private static double	 lastTime = System.currentTimeMillis();
	private static int aa = 0;
	
	public static XYSeries getSeries() {
		return eegSeries;
	}
	
	public static XYPlot getPlot() {
		return eegPlot;
	}
	//


	// Define the ticks for this chart.
	public void setTicks(String axis, int size) {

		NumberAxis numberAxis;
		XYPlot p = getPlot();

		/*
		if (axis == "domain") { 
			numberAxis = (NumberAxis) getPlot().getDomainAxis(); 
		}

		else { 
			numberAxis = (NumberAxis) getPlot().getRangeAxis(); 
		}
		 */

		numberAxis = (axis == "domain") ? (NumberAxis) p.getDomainAxis() 
				: (NumberAxis) p.getRangeAxis();

		numberAxis.setTickUnit(new NumberTickUnit(size, new DecimalFormat("##")));
	}
	//


	// Add a new value to the plot.
	public static void addEeg(double value) 
	{
		
		eegSeries.addOrUpdate(aa, value);
		aa += 1;
		/*

		if (aa >= 2589) {
			aa = 0;
			getPlot().clearAnnotations();
			getPlot().clearDomainMarkers();
		}
		*/
	}
	//
	
	
	// Add a marker to the plot when a new channel is selected.
	public static void addMarker(int channel) {

		XYPlot p = getPlot();

		
		// Create an annotation with the new channel's name.
		XYTextAnnotation ann = new XYTextAnnotation(Interface.getChannelName(channel), aa+0.8, -185.0);
		ann.setPaint(Color.WHITE);
		//
		
		
		// Create and insert the marker on the plot.
		Marker marker = new IntervalMarker(aa-0.5, aa+0.5, Color.decode("#B54545"));
		p.addDomainMarker(marker);
		p.addAnnotation(ann);
	}
	//
	
	
	// Override the data set creation method according to the eeg properties.
	@Override
	public void createDataSet() 
	{
		setDataSet(new XYSeriesCollection());
		eegSeries = new XYSeries("EEG", false, false);
		//eegSeries.setMaximumItemCount(1500);
		getDataSet().addSeries(eegSeries);
	}
	
	@Override
	public void defineChart(XYPlot plot)
	{
		eegPlot = plot;
	}
	//
	
	
	// Raw EEG data chart
	public EegChart() {
		eegSeries.clear();
		setPanelSize(860, 250);
		setDomainLabel("Time (s)");
		setRangeVisible(true);
		setDomainRange(0,  1500, true);
		setRangeRange(-508.0, 508.0, false);
		setRangeCenter(0.0);
		setTicks("domain", 1);
	}
	//
}
