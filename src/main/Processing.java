package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.complex.Complex;

import edu.emory.mathcs.jtransforms.fft.DoubleFFT_1D;


public class Processing {
	
	/*
	private static List<Integer> 	zeroCross 		= new ArrayList<Integer>(Collections.nCopies(14,0));
	private static double			fastAverage		= 0.0;
	private static int				averageCount	= 0;
	private static double			lastTime		= System.currentTimeMillis();
	private static List<Double> 	lastChanValue 	= new ArrayList<Double>(Collections.nCopies(14,0.0));
	*/
	
	// Initiate variables.
	private static List<Double> 	channelValue 	= new ArrayList<Double>(Collections.nCopies(14,0.0));
	private static List<Double> 	channelAverage 	= new ArrayList<Double>(Collections.nCopies(14,0.0));
	
	private static int 				channelCount 	= 0;
	private static int 				CHANNEL			= 0;

	private static final int 		IRR_COUNT  		= 214;
	private static double[] 		fftList 		= new double[128];
	
	private static boolean			recording		= false;
	private static boolean			squareActive	= true;
	//

	
	
	///// Left wink detection.
	///
	//
	
	
	// wL_value holds the reference values to feed the LDA algorithm
	// with a trigger event and a baseline.
	private final double[][] wL_value = {
			
			// Trigger event.
			{ 26.0, 600.0}, { 30.0, 900.0 }, { 66.0, 922.0 }, { 360.0, 1000.0 }, { 360.0, 1900.0 },
			{ 400.0, 1300.0 }, { 843.0, 216.0 }, { 1012.0, 261.0 },  { 1400.0, 2000.0 }, { 1550.0, 3200.0 }, 

			// Baseline.
			{ 3.0, 5.0 }, { 6.0, -13.0 }, { 1.0, 8.0 }, { 20.0, 0.0 }, { 3.5, 5.6 }, 
			{ 3.5, 11.0 }, { 70.0, 40.0 }, { 70.0, 100.0 }, { 190.0, 340.0 }, { 320.0, 580.0 }
			
	};
	//
	
	
	// wL_class indicates the classes of wL_value values.
	private final int[] wL_class = { 
			1,1,1,1,1,1,1,1,1,1,
			2,2,2,2,2,2,2,2,2,2
	};
	//
	
	
	// wL_data analyses the stored data to be able to classify future data.
	LDA wL_data = new LDA(wL_value, wL_class, true);

	
	// Initiate needed variables.
	int wL_result;
	
	double wL_ch0;
	double wL_ch1;
	double wL_ch2;
	double wL_average;
	double wL_ratio;
	
	static int winkLeftOn = 0;
	//
	
	
	// Event detection function.
	// This function is fed with a list of values every time a sample is acquired.
	// Then it checks for specific feature in order to determine if an event occurred.
	private boolean isWinkLeft(List<Double> values) {
		
		
		// If a recent detection occurred, skip this one.
		if (winkLeftOn > 0) {
			return false;
		}
		//
		
		
		// Compare specific channels' values.
		wL_ch0 = values.get(0);
		wL_ch1 = values.get(1);
		wL_ch2 = wL_ch0 + wL_ch1;
		
		if (wL_ch2 < 0) return false;
		if (wL_ch0 > wL_ch1) return false;
		//
		
		
		// Channels' average calculations.
		wL_average = Math.abs(chanRatioAverage(0,1, values));
		
		if (wL_average < 10.0) return false;
		if (wL_average > wL_ch2) return false;
		//
		
		// LDA calculations.
		double[] wL_testdata = { wL_average, wL_ch2 };
		double[] wL_values = wL_data.getDiscriminantFunctionValues(wL_testdata);
		
		wL_ratio = wL_values[0] / wL_values[1];
		
		if (wL_ratio < 0) return false;

		
		wL_result = wL_data.predict(wL_testdata);
		
		if (wL_result == 2) return false;
		if (wL_result == 3) return false;
		//
		
		
		/* Outputs
		System.out.println("-");
		System.out.println(ch2);
		System.out.println(cAverage);
		System.out.println(vratio);
		System.out.println(values2[0]);
		System.out.println(values2[1]);
		
		for (int x = 0; x < values.size(); x++) {
			System.out.println(values.get(x));
		}
		*/
		
		
		// Event detected.
		winkLeftOn = 60;
		return true;
	}
	//


	// Getters & Setters.
	/*
	public static double getLastTime() 
	{
		return lastTime;
	}
		public static List<Double> getLast() {
		return lastChanValue;
	}
	
	public static double getLast(int x) {
		return lastChanValue.get(x);
	}
	
	public static void setLast(int x, double value) {
		lastChanValue.add(x, value);
	}
	*/
	public static void setChannel(int x) 
	{ 
		CHANNEL = x; 
	}
	
	public static boolean getRecording()
	{
		return recording;
	}
	
	public static void setRecording(boolean b)
	{
		recording = b;
	}
	
	public static void setAverage(int x, double value) {
		channelAverage.set(x,value);
	}
	
	public static double getAverage(int x) {
		return channelAverage.get(x);
	}
	
	public static double getCurrent() {
		return channelValue.get(CHANNEL);
	}
	
	public static void setCount(int x) {
		channelCount += x;
	}
	
	public static void setCount() {
		channelCount = 0;
	}
	
	public static int getCount() {
		return channelCount;
	}
	
	public static List<Double> getValue() {
		return channelValue;
	}
	
	public static double getValue(int x) {
		return channelValue.get(x);
	}
	
	public static int getChannel() 
	{ 
		return CHANNEL; 
	}
	
	public static List<Double> getAverage() {
		return channelAverage;
	}
	
	public static void setValue(int x, double value) {
		channelValue.add(x, value);
	}
	//
	
	
	// Calculate a specific channel divided by the rest.
	// Input is the list of all values of a specific sample.
	public double chanRatio(int channel, List<Double> values) {
		double ratio;
		double total = 0;
		
		for (int x = 0; x < 14; x++) {
			if (x != channel) total += values.get(x);
		}
		
		ratio = values.get(channel)/total;
		
		return ratio;
	}
	//
	
	
	// Calculates a specific channel divided by the rests' average.
	public double chanRatioAverage(int channel1, int channel2, List<Double> values) {
		double ratio;
		double total = 0;
		double average = 0;
		
		for (int x = 0; x < 14; x++) {
			if (x != channel1 && x != channel2) total += values.get(x);
		}
		average = total/(values.size()-2);
		ratio = values.get(channel2)/average;
		
		return ratio;
	}
	//
	
	
	// Slope.
	private static double calcSlope(double[] values) {
		double high = values[-1];
		double low = values[0];
		
		return (high-low)/values.length;
	}
	//
	
	
	// Standard deviation.
	private static double calcSd(double[] values) {
		double total = 0.0;
		double average = calcAverage(values);
		for (Double y: values) total += Math.pow((y - average), 2);
		
		return Math.sqrt(total/values.length - 1);
	}
	//

	
	// Average.
	private static double calcAverage(double[] values) {
		double total = 0.0;
		for (Double x: values) total += x;
		
		return total/values.length;
	}
	//
	
	
	// Positive/negative average
	private static double calcAverage(double[] values, boolean positive) {
		
		double total = 0.0;
		int count = 0;
		
		if (positive) {
			for (Double x: values) {
				if (x >= 0.0) {
					total += x;
					count += 1;
				}
			}
		}
		else {
			for (Double x: values) {
				if (x < 0.0) {
					total += x;
					count += 1;
				}
			}
		}
		
		return total/count;
		
	}
	//
	
	
	// Calculates the highest brain band.
	private static String findHigh(double[] bands) {
		
		
		// Find highest band.
		double high = 0.0;
		int index = 0;
		for (int x = 0; x < bands.length; x++) {
			if (bands[x] > high) {
				high = bands[x];
				index = x;
			}
		}
		//
		
		
		// Return band name.
		switch (index) {
		case 0: return "Delta";
		case 1: return "Alpha";
		case 2: return "Theta";
		case 3: return "Beta";
		default: return "(n/a)";
		}
		//
	}
	//
	
	
	// Calculate each brain band's value.
	private static void calcBands(double[] magValues) {


		// Initiate bands variables.
		double delta = 0.0;
		double alpha = 0.0;
		double theta = 0.0;
		double beta  = 0.0;
		//
		
		
		// Add each band's values.
		for (int x = 1; x < magValues.length/2; x++) {
			
			// Delta waves: up to 4 Hz.
			if (x < 5) delta += magValues[x];
			
			// Theta waves: 4-8 Hz.
			else if (x < 9) theta += magValues[x];
			
			// Alpha waves: 8-13 Hz.
			else if (x < 14) alpha += magValues[x];
			
			// Beta waves: 14-30 Hz.
			else if (x < 31) beta += magValues[x];
			
			// Ignore the other frequencies.
			else break;
		}
		//
		
		
		// Average the results.
		delta = roundTwo(delta/4);
		theta = roundTwo(theta/4);
		alpha = roundTwo(alpha/5);
		beta  = roundTwo(beta/17);
		//
		
		
		// Display the results.
		BarChart.addValue("Delta", delta);
		BarChart.addValue("Theta", theta);
		BarChart.addValue("Alpha", alpha);
		BarChart.addValue("Beta", beta);

		double[] bands = { delta, alpha, theta, beta };
		Values.setBandValue(findHigh(bands));
		
		//if (getRecording()) Saving.bands(bands);
		//
	}
	//
	
	
	// Hann window function.
	private static double[] setHann(double[] rawEeg) {
		
		double[] window = new double[rawEeg.length];
		
		for (int i = 0; i<rawEeg.length; i++) {
			double multiplier = 0.5 * (1 - Math.cos(2*Math.PI*i/rawEeg.length));
			window[i] = multiplier * rawEeg[i];
		}
		
		return window;
	}
	//
	
	
	/*
	// Set Hann window function.
	private static double[] setHann(double[] rawEeg) {
		
		
		double[] window = new double[128];
		double[] weight = new double[128];
		
		int m = rawEeg.length;
		int n = m/2-1;
		int j = 0;
		
		
		// Loop through every value, applying specific weights.
		for (int i = 0; i<n; i++) {
			
			// Hann function: (1-cos(2*i*PI/n))*0.5
			weight[i] = (1.0D - Math.cos(2.0D*i*Math.PI/n))*0.5D;
			window[j] = rawEeg[j++]*weight[i];
			window[j] = rawEeg[j++]*weight[i];
		}
		//
		
		
		return window;
	}
	//
	 
	 */
	
	
	// Forward FFT.
	private static Complex[] fftForward(double[] values) {
		
		
		DoubleFFT_1D 	transf 			= new DoubleFFT_1D(values.length);
		double[] 		signalArraySeq 	= new double[values.length*2];
		
		
		// Fills an array with the provided values.
		for (int i=0; i < values.length; i++) {
			signalArraySeq[2*i] = values[i];
			signalArraySeq[2*i+1] = 0.0;
		}
		//
		
		
		// Performs forward FFT.
		transf.complexForward(signalArraySeq);
		
		// Creates a complex array to store the values.
		Complex[] transformadaCpx = new Complex[values.length];
		
		// Stores the calculated FFT in the complex array.
		for (int i=0; i < values.length; i++) {
			transformadaCpx[i] = new Complex(signalArraySeq[2*i], signalArraySeq[2*i+1]);
		}
		return transformadaCpx;
	}
	//
	
	
	/* Inverse FFT.
	
	private static double[] fftInverse(Complex[] values) {
		
		DoubleFFT_1D 	transf 		= new DoubleFFT_1D(values.length);
		double[] 		valuesArray = new double[values.length*2];
		
		for (int i=0; i<values.length; i++) {
			valuesArray[2*i] = values[i].getReal();
			valuesArray[2*i+1] = values[i].getImaginary();
		}
		
		transf.complexInverse(valuesArray, true);
		
		double[] reconstructed = new double[values.length];
		
		for (int i =0; i< values.length; i++) {
			reconstructed[i] = valuesArray[2*i];
		}
		
		return reconstructed;
	}	
	*/
	
	
	// Magnitude from FFT.
	private static double[] getMagnitude(Complex[] list) {
		
		
		// Initiate variables.
		double[]  magList = new double[128];
		double 	  magReal = 0.0;
		double 	  magImag = 0.0;
		//
		
		
		// Calculate the magnitude for the provided list.
		for (int x = 0; x < list.length; x ++) {

			// Calculate the magnitude using M = sqrt(real^2 + imag^2).
			magReal    = Math.pow(list[x].getReal(), 2);
			magImag    = Math.pow(list[x].getImaginary(), 2);
			magList[x] = Math.sqrt(magReal + magImag);
			
			// Display the results.
			FftChart.addFft(x, magList[x]);
		}
		//
		
		return magList;
	}
	//
	
	/*
	private void calcFast(double value) {
		
		fastAverage  += value;
		averageCount += 1;
		

		// Calcs the variation each second.
		if (averageCount%128 == 0) 
		{
			fastAverage /= 128;
			Values.setLateValue(roundTwo(fastAverage));
			fastAverage = 0.0;
		}
		//
		
		
		// Calcs the variation in real-time.
		else {
			Values.setInstantValue(value);
		}
	}
	//
	*/
	
	// Rounds a value to 2 decimal places.
	private static double roundTwo(double value) 
	{
		return Math.round(value*100.0)/100.0;
	}
	//

	
	// Main function.
	public Processing(List<Double> values) {
		
		
		// Initiate needed values.
		Complex[] fftForward = new Complex[128];
		double[]  hannWindow = new double[128];
		double[]  magValues	 = new double[128];
		
		double value;
		//
		
		
		// Calculates and removes the background from a range of 214 samples,
		// for a 0.6Hz high pass filter (1/0.6*128).
		for (int x = 0; x < 14; x++) {
			
			
			// Local variables.
			value = values.get(x);
			//
			
			
			// Save current value and average.
			setAverage(x, ((getAverage(x) * (IRR_COUNT-1)) + value) / IRR_COUNT);
			setValue(x, roundTwo(value - getAverage(x)));
			//
			
			
			// Records the data to a file.
			if (getRecording()) Saving.addRaw(getValue(x), x);
			//
				
			/*
			// Register zero crossing.
			if ((last > 0 && channelValue.get(x) < 0) || (last < 0 && channelValue.get(x) > 0)) 
			{
				zeroCross.set(x, zeroCross.get(x)+1);
			}
			//
			*/
			
			//
		}
		//				
		EegChart.addEeg(getCurrent());
		
		
		// Left wink detection.
		if (winkLeftOn > 0) winkLeftOn -= 1;
		if (isWinkLeft(getValue())) {
			System.out.println("WINK LEFT");
		}
		//
		
		
		// Adds each sample to a list until 128 samples are recorded.
		fftList[channelCount] = getCurrent();
		setCount(1);
		//
		
		
		// Display each channel's activity.
		if (squareActive) {
			Square square = Interface.square;

			square.squareRepaint(channelValue);
		}
		//


		// After collecting 128 samples.
		if (getCount() >= 128) {

			// Applies Hann window function, calculates FFT and magnitude.
			hannWindow = setHann(fftList);
			
			
			fftForward = fftForward(hannWindow);
			magValues  = getMagnitude(fftForward);
			
			calcBands(magValues);
			setCount();
			//
		}
	}
}
