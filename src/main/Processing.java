package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.complex.Complex;

import edu.emory.mathcs.jtransforms.fft.DoubleFFT_1D;


public class Processing {

	//private static List<Integer> 	zeroCross 		= new ArrayList<Integer>(Collections.nCopies(14,0));
	//private static double			fastAverage		= 0.0;
	//private static int			averageCount	= 0;
	//private static double			lastTime		= System.currentTimeMillis();
	//private static List<Double> 	lastChanValue 	= new ArrayList<Double>(Collections.nCopies(14,0.0));

	
	// Initiate variables.
	private static List<Double> 	channelValue 	= new ArrayList<Double>(Collections.nCopies(14,0.0));
	private static List<Double> 	channelAverage 	= new ArrayList<Double>(Collections.nCopies(14,0.0));
	private static int 			CHANNEL			= 0;

	private static int 			channelCount 	= 0;
	private static final int 	IRR_COUNT  		= 214;
	private static double[] 		fftList 		= new double[128];
	
	private static boolean		recording		= false;
	private static boolean		squareActive	= true;
	//
	
	
	//// Left wink detection.
	
	// Reference values to feed the LDA algorithm.
	private final double[][] wL_value = {
			
			// Trigger event.
			{ 26.0, 600.0}, { 30.0, 900.0 }, { 66.0, 922.0 }, { 360.0, 1000.0 }, { 360.0, 1900.0 },
			{ 400.0, 1300.0 }, { 843.0, 216.0 }, { 1012.0, 261.0 },  { 1400.0, 2000.0 }, { 1550.0, 3200.0 }, 
			{250.0, 950.0 }, {40.0, 700.0 },
			 
			// Baseline.
			{ 3.0, 5.0 }, { 6.0, -13.0 }, { 1.0, 8.0 }, { 20.0, 0.0 }, { 3.5, 5.6 }, 
			{ 3.5, 11.0 }, { 70.0, 40.0 }, { 70.0, 100.0 }, { 190.0, 340.0 }, { 320.0, 580.0 },
			{120.0, 160.0 }
	};
	//

	
	// Indicates the classes for wL_value.
	private final int[] wL_class = { 
			1,1,1,1,1,1,1,1,1,1,1,1,
			2,2,2,2,2,2,2,2,2,2,2,
	};
	//

	
	// Analyses the stored data to classify acquired data.
	LDA wL_data = new LDA(wL_value, wL_class, true);
	//
	
	
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
		if (winkLeftOn > 0) return false;
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

	
	//// Right wink detection.
	
	
	// Reference values to feed the LDA algorithm.
	private final double[][] wR_value = {

			// Trigger event.
			{ 13.0, 5.0, 82.0 },   { 5.0, 3.0, 162.0 },  { 21.0, 6.0, 197.0 },  { 2.0, 4.0, 168.0 },   {25.0, 2.0, 230.0 },
			{ 8.0, 8.0, 106.0 },   { 13.0, 4.0, 470.0 }, { 14.0, 0.5, 300.0 },  { 12.0, 0.0, 183.0 },  { 3.0, 2.0, 205.0 },
			{ 5.0, 5.0, 245.0 },   { 5.0, 17.0, 383.0 }, { 16.0, 0.0, 257.0, }, { 22.0, 0.0, 318.0 },  { 20.0, 4.0, 375.0 },
			{ 19.0, 2.0, 300.0 },  { 1.0, 0.5, 435.0 },  { 2.0, 5.0, 395.0 },   { 11.0, 3.0, 87.0 },   { 5.0, 1.0, 436.0 },
			{ 1.0, 4.0, 200.0 },   { 16.0, 6.0, 207.0 }, { 1.0, 4.0, 164.0 },   { 26.0, 6.0, 134.0 },  { 15.0, 3.0, 250.0 },
			{ 13.0, 9.0, 100.0 },  { 16.0, 5.0, 85.0 },  { 11.0, 7.0, 96.0 },   { 6.0, 8.0, 198.0 },   { 10.0, 8.0, 303.0 },
			{ 3.0, 0.5, 146.0 },   { 2.0, 12.0, 172.0 }, { 4.0, 7.0, 300.0 },   { 4.0, 17.0, 100.0 },  { 16.0, 4.0, 190.0 },
			{ 13.0, 13.0, 107.0 }, { 46.0, 5.0, 100.0 }, { 40.0, 3.0, 222.0 },  { 13.0, 12.0, 170.0 }, { 7.0, 5.0, 225.0 },
			{ 26.0, 1.0, 113.0 },  { 14.0, 3.0, 200.0 }, { 5.0, 2.0, 164.0 },   { 27.0, 2.0, 198.0 },

			// Baseline.
			{ -15.0, -5.0, 50.0 },  { -26.0, -12.0, 60.0 }, { -23.0, -7.0, 55.0 },  { -17.0, -17.0, 62.0 },
			{ -14.0, -8.0, 52.0 },  { -30.0, -3.0, 55.0 },  { -13.0, -10.0, 61.0 }, { -15.0, -7.0, 57.0 },
			{ 95.0, 2.5, 120.0 },   { 154.0, 30.0, 160.0 }, { 18.0, 0.5, 90.0 },    { 47.0, 8.0, 83.0 },
			{ 36.0, 14.0, 158.0 },  { -19.0, -12.0, -3.0 }, { -20.0, -4.0, 11.0 },  { -20.0, -12.0, 6 },
			{ 5.0, -15.0, 4.0 },    { 7.0, -24.0, -6 },     { 32.0, 34.0, -9 },     { 47.0, 44.0, 18.0 },
			{ 18.0, 12.0, -2 },     { 16.0, 14.0, -5.0 },   { 29.0, 16.0, 9.0 },    { 54.0, 13.0, -23.0 },
			{ -12.0, 35.0, -26.0 }, { 50.0, 6.0, 83.0 },    { 43.0, 11.0, 80.0 },

			// Initial data.
			{ 300.0, 370.0, 300.0 }, { 4600.0, 4600.0, 4200.0 }, { 4500.0, 4500.0, 4200.0 }, { 3600.0, 3600.0, 3300.0 }
	};
	//

	// Indicates the classes for wR_value.
	private final int[] wR_class = { 
			
			// Trigger event.
			1,1,1,1,1,1,1,1,1,1,
			1,1,1,1,1,1,1,1,1,1,
			1,1,1,1,1,1,1,1,1,1,
			1,1,1,1,1,1,1,1,1,1,
			1,1,1,1,
			
			// Baseline.
			2,2,2,2,2,2,2,2,2,2,
			2,2,2,2,2,2,2,2,2,2,
			2,2,2,2,2,2,2,
			
			// Initial data.
			3,3,3,3
	};
	//
	
	
	// Analyses the stored data to classify acquired data.
	LDA wR_data = new LDA(wR_value, wR_class, true);
	//
	
	
	// Initiate needed variables.
	int wR_result;
	
	double wR_ch4;
	double wR_ch5;
	double wR_ch13;
	
	double wR_ch13d4;
	double wR_ch13d5;
	double wR_ch13d6;
	double wR_ch13d10;
	
	double wR_average;
	
	double wR_ch13_min = 80.0;
	double wR_ch5_min  = 0.0;
	double wR_ch5_max  = 20.0;
	double wR_ch4_min  = 0.0;
	double wR_ch4_max  = 50.0;
	
	static int winkRightOn = 0;
	//
	
	
	// Event detection function.
	private boolean isWinkRight(List<Double> values) {
		
		
		// If a recent detection occurred, skip this one.
		if (winkRightOn > 0) return false;
		//
		

		// Comparing channels of interest.
		wR_ch4  = values.get(3);
		wR_ch5  = values.get(4);
		wR_ch13 = values.get(12);

		double[] wR_testdata = { wR_ch4, wR_ch5, wR_ch13 };
		wR_result = wR_data.predict(wR_testdata);
		
		if (wR_ch13 < wR_ch13_min) return false;
		if (wR_ch5 < wR_ch5_min || wR_ch5 > wR_ch5_max) return false;
		if (wR_ch4 < wR_ch4_min || wR_ch4 > wR_ch4_max) return false;
		//
		
		
		/*
		if (wR_result == 1) { 
			System.out.println(wR_ch4);
			System.out.println(wR_ch5);
			System.out.println(wR_ch13);
			System.out.println("-");
		}
		*/
		// Channels of interest ratios.
		//wR_ch13d4  = wR_ch13/wR_ch4;
		//wR_ch13d5  = wR_ch13/wR_ch5;
		//wR_ch13d6  = wR_ch13/values.get(5);
		//wR_ch13d10 = wR_ch13/values.get(9);
		
		/*
		if (wR_ch13d4 < 5) return false;
		if (wR_ch13d5 < 5) return false;
		if (wR_ch13d6 > 0 && wR_ch13d6 < 10) return false;
		if (wR_ch13d10 > 0 && wR_ch13d10 < 10) return false;
		*/
		//
		
		
		// LDA calculations.

		//double[] wR_values = wR_data.getDiscriminantFunctionValues(wR_testdata);
		/*
		//if (wR_values[0] < 20.0 || wR_values[0] > 30.0) return false;
		//if (wR_values[1] > -0.45) return false;


		
		if (wR_ch13/wR_values[0] > 3) return false;
		if (wR_ch13/wR_values[1] < -200) return false;
		*/

		
		
		if (wR_result != 1) return false;
		//
		/*
		for (int x = 0; x < values.size(); x++) {
			System.out.println(values.get(x));
		}
		
		System.out.println(wR_result);
		*/
		//System.out.println(wR_average);
		//System.out.println(wR_values[0]);
		//System.out.println(wR_values[1]);
		//System.out.println("-");
		
		winkRightOn = 60;
		return true;
	}
	//
	

	// Getters & Setters.
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
	/*
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
	
	
	// Left wink detection.
	static int phase1 = 0;
	static int phase2 = 0;
	static int phase3 = 0;
	static int phase33 = 0;
	static int phase4 = 0;
	
	static 	int blankCount = 0;
	
	static double minWinkValue = -50.0;
	static double winkThreshold = 30.0;
	static double maxWinkValue = 100.0;
	
	private static void leftWink(double value, int channel) {
		
		int x = channel;
		
		
		// Channel 0.
		if (x == 0) {
			if (phase2 == 0) {
				if (value >= winkThreshold) {
					phase1 = 1;
					//winkRec = 1;
				}
			}
		}
		//
		
		
		// Channel 1.
		else if (x == 1) {
			if (phase3 == 0) {
				if (value >= maxWinkValue) {
					phase2 = 1;
				}
			}
			if (phase3 == 1) {
				if (value < minWinkValue) {
					phase4 = 1;
				}
			}
		}
		//
		
		
		// Channel 12.
		else if (x == 12) {
			if (value > 80.0) {
				leftWink(false);
			}
		}
		//
		
		
		// Other channels.
		else if (phase2 == 1 && phase3 == 0) {
			if (value < 25.0 && value > -25.0) {
				blankCount +=1;
			}
		}
	}
	//
	
	
	//
	private static void leftWink(int a) {
		
		
		// Perform actions before a set of data is recorded.
		if (a == 0) {
			blankCount = 0;

			if (phase33 > 0) phase33 +=1;

			if (phase33 >= 50) leftWink(false);
		}
		//
		
		
		// Perform actions after the dataset is recorded.
		if (a == 1) {
			if (phase2 == 1 && phase3 == 0) {
				if (blankCount == 11 || blankCount == 12) {
					phase3 = 1;
					phase33 = 1;
				}
				else leftWink(false);
			}
			if (phase4 == 1) {
				//System.out.println(winkValues);
				System.out.println("WINK");

				leftWink(false);
			}
		}
	}
	
	private static void leftWink(boolean a) {
		if (!a) {
			phase33 = 0;
			phase1 = 0;
			phase2 = 0;
			phase3 = 0;
			phase4 = 0;
			//winkRec = 0;
			
			//winkValues.clear();
		}
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
	
	private static double[] setHann(double[] rawEeg) {
		
		double[] window = new double[rawEeg.length];
		
		for (int i = 0; i<rawEeg.length; i++) {
			double multiplier = 0.5 * (1 - Math.cos(2*Math.PI*i/rawEeg.length));
			window[i] = multiplier * rawEeg[i];
		}
		
		return window;
	}
	
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
		double[] magList = new double[128];
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

	
	// Removes background from raw EEG data.
	public Processing(List<Double> values) {
		
		
		// Initiate needed values.
		Complex[] fftForward = new Complex[128];
		double[] hannWindow = new double[128];
		double[] magValues	 = new double[128];
		
		double value;

		
		// Calculates and removes the background from a range of 214 samples,
		// for a 0.6Hz high pass filter (1/0.6*128).
		for (int x = 0; x < 14; x++) {
			
			
			// Local variables.
			value = values.get(x);
			//
			
			
			// Save current value and average.
			setAverage(x, ((getAverage(x) * (IRR_COUNT-1)) + value) / IRR_COUNT);
			channelValue.set(x, roundTwo(value - getAverage(x)));


			if (getRecording()) Saving.addRaw(getValue(x), x);
				
			/*
			// Register zero crossing.
			if ((last > 0 && channelValue.get(x) < 0) || (last < 0 && channelValue.get(x) > 0)) 
			{
				zeroCross.set(x, zeroCross.get(x)+1);
			}
			//
			*/
			
			// Save last recorded value.
			//setLast(x, getValue(x));
			//
		}
		//				
		EegChart.addEeg(getCurrent());
		
		if (winkLeftOn > 0) winkLeftOn -= 1;
		if (isWinkLeft(getValue())) {
			System.out.println("WINK LEFT");
		}
		
		if (winkRightOn > 0) winkRightOn -= 1;
		if (isWinkRight(getValue())) {
			System.out.println("WINK RIGHT");
		}
		
		
		// Adds each sample to a list until 128 samples are recorded.
		fftList[channelCount] = getCurrent();
		setCount(1);
		//
		
		
		// Displays the raw EEG data.
		

		
		//calcFast(currentChan);
		//


		// Display each channel's activity.
		if (squareActive) {
			Square square = Interface.square;

			square.squareRepaint(channelValue);
		}
		//

		/*
		// Calculates max and min values.
		if (currentChan > Values.getMaxValue()) 
		{ 
			Values.setMaxValue(currentChan);
		}

		if (currentChan < Values.getMinValue()) 
		{
			Values.setMinValue(currentChan);
		}
		//
	*/

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
