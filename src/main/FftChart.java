package main;

import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

@SuppressWarnings("serial")
public class FftChart extends XYChart {
	
	
	private static XYSeries fftSeries;
	
	
	// Subclass methods
	public static void addFft(int freq, double value) 
	{
		fftSeries.addOrUpdate(freq, value);
	}
	
	@Override
	public void createDataSet() 
	{
		setDataSet(new XYSeriesCollection());
		fftSeries = new XYSeries("FFT", false, false);
		getDataSet().addSeries(fftSeries);
	}
	//
	
	
	// Fourier transform chart
	FftChart() {
		setPanelSize(300, 250);
		setDomainLabel("Magnitude (uV) / Frequency (Hz)      ");
		setDomainRange(0.0, 64.0, false);
		setRangeRange(0.0, 205.0, false);
		setRangeVisible(true);
	}
	//
}
