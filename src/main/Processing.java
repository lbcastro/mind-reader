package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.complex.Complex;


public class Processing {

	//private static List<Integer> 	zeroCross 		= new ArrayList<Integer>(Collections.nCopies(14,0));
	//private static double			fastAverage		= 0.0;
	//private static int			averageCount	= 0;
	//private static double			lastTime		= System.currentTimeMillis();
	//private static List<Double> 	lastChanValue 	= new ArrayList<Double>(Collections.nCopies(14,0.0));
	
	
	// Static variables.
	private static List<Double> 	channelValue 	 = new ArrayList<Double>(Collections.nCopies(14,0.0));
	private static List<Double> 	channelAverage 	 = new ArrayList<Double>(Collections.nCopies(14,0.0));
	private static int 			channelNumber	 = 0;
	private static int 			channelCount 	 = 0;
	
	private static final int 	IRR_COUNT  		 = 214;
	
	private static boolean		recording		 = false;
	private static boolean		calcFft			 = false;
	
	private static boolean		VISUALIZE		 = false;
	private static boolean		VISUALIZE_EEG	 = true;
	private static boolean		VISUALIZE_FFT	 = true;
	private static boolean		VISUALIZE_SQUARE = true;
	private static boolean		VISUALIZE_BANDS	 = true;
	
	private static boolean		detectWL		 = true;
	private static boolean		detectWR		 = true;
	
	static int 					recentEvent		 = 0;
	//
	
	
	// Left wink detection.

	// Channels 2 and 13 - values.
	private final double[][] wL_value1 = {
			
			
			// Trigger event.
			{ 117.44, 91.35 }, { 67.36, 41.99 },  { 104.81, 37.35 }, { 63.72, 18.15 },  { 89.88, 19.11 },
			{ 92.71, 20.37 },  { 91.31, 1.45 },   { 89.94, 14.15 },  { 74.34, 2.33 },   { 69.16, 12.14 },
			{ 75.34, 3.4 },    { 61.38, -0.73 },  { 123.65, -4.04 }, { 138.13, 22.87 }, { 133.19, 8.26 },
			{ 85.6, 6.82 },    { 72.21, 12.34 },  { 61.79, 35.99 },  { 89.59, 14.81 },  { 240.16, 17.66 },
			{ 234.35, 24.62 }, { 231.06, 23.22 }, { 202.23, 7.11 },  { 93.13, -15.95 }, { 105.0, -18.29 },
			//
			
			
			// Initial data filtering.
			{ 884.42, 920.91 }, { 873.64, 950.01 }, { 858.71, 778.07 }, { 857.85, 988.51 }, { 851.32, 764.69 },
			{ 841.07, 747.0 },  { 831.33, 752.09 }, { 826.64, 742.1 },  { 807.2, 1004.31 }, { 792.06, 721.97 }, 
			{ 787.41, 722.65 }, { 778.27, 995.43 }, { 775.51, 706.07 }, { 770.27, 683.77 }, { 763.6, 671.82 },
			{ 993.31, 657.38 }, { 695.56, 977.87 }, { 654.28, 957.57 }, { 617.37, 955.75 }, { 575.96, 930.54 },
			{ 536.98, 893.33 }, { 491.75, 812.68 }, { 471.07, 719.09 }, { 454.46, 668.57 }, { 428.82, 610.91 },
			{ 401.41, 553.28 }, { 382.39, 498.71 }, { 376.65, 107.41 }, { 359.97, 429.4 },  { 335.75, 370.9 },
			{ 308.16, 329.77 }, { 307.32, 187.71 }, { 304.53, 201.35 }, { 398.64, 213.24 }, { 284.78, 208.23 },
			{ 283.2, 277.37 },  { 282.62, 225.49 }, { 280.34, 282.92 }, { 268.88, 238.69 }, { 266.86, 195.12 },
			{ 259.35, 254.93 }, { 249.72, 263.46 }, { 239.39, 274.81 }, { 232.25, 174.61 }, { 227.54, 223.34 },
			{ 225.72, 285.92 }, { 216.32, 157.19 }, { 200.18, 291.93 }, { 188.31, 181.13 }, { 182.38, 439.92 },
			{ 173.61, 140.84 }, { 166.23, 306.5 }
			//
			
			
	};
	//
	
	
	// Channels 2 and 13 - classification.
	private final int[] wL_class1 = {

			
			// Trigger event.
			1,1,1,1,1,1,1,1,1,1,
			1,1,1,1,1,1,1,1,1,1,
			1,1,1,1,1,
			//


			// Initial data filtering.
			2,2,2,2,2,2,2,2,2,2,
			2,2,2,2,2,2,2,2,2,2,
			2,2,2,2,2,2,2,2,2,2,
			2,2,2,2,2,2,2,2,2,2,
			2,2,2,2,2,2,2,2,2,2,
			2,2
			//


	};
	//
	
	
	// Channels 2 and 4 - values.
	private final double[][] wL_value2 = {
		
			
			// Trigger event.
			{ 117.44, 1.54 },  { 67.36, 5.69 },  { 104.81, 1.83 },  { 63.72, 1.94 },   { 89.88, 3.6 },
			{ 92.71, 3.01 },   { 91.31, 17.25 }, { 89.94, 18.61 },  { 74.34, 8.27 },   { 69.16, 16.26 },
			{ 75.34, 3.25 },   { 61.38, 0.17 },  { 123.65, 38.62 }, { 138.13, 24.84 }, { 133.19, 2.74 },
			{ 85.6, 0.8 },     { 72.21, 14.48 }, { 61.79, 7.68 },   { 89.59, 3.61 },   { 240.16, 9.11 }, 
			{ 234.35, 15.63 }, { 231.06, 8.78 }, { 202.23, 5.77 },  { 93.13, 13.21 },  { 105.0, 18.09 },
			//
			
			
			// Baseline removal.
			{ 126.87, 90.99 }, { 99.16, 90.67 }, { 110.19, 88.76 }, { 117.88, 83.08 }, { 89.2, 79.45 }, 
			{ 89.96, 77.83 },  { 86.81, 70.55 }, { 70.06, 63.88 },  { 75.08, 60.55 },  { 68.28, 51.07 },
			{ 54.49, 40.98 },  { 45.94, 32.21 }, { 42.55, 30.55 },  { 40.96, 27.04 },  { 80.77, 23.33 },
			{ 42.16, 20.53 },  { 60.28, 18.75 }, { 50.73, 18.15 },  { 43.5, 15.81 },   { 41.83, 10.01 }
			//
			
			
	};
	//
	
	
	// Channels 2 and 4 - classification.
	private final int[] wL_class2 = {

			// Trigger event.
			1,1,1,1,1,1,1,1,1,1,
			1,1,1,1,1,1,1,1,1,1,
			1,1,1,1,1,
			//


			// Baseline removal.
			2,2,2,2,2,2,2,2,2,2,
			2,2,2,2,2,2,2,2,2,2,
			//
			
			
	};
	//
	
	
	// Classifies stored data.
	LDA wL_data1 = new LDA(wL_value1, wL_class1, true);
	LDA wL_data2 = new LDA(wL_value2, wL_class2, true);
	//
	
	
	// Initiate needed variables.
	int wL_result;
	
	double wL_ch2;
	double wL_ch4;
	double wL_ch11;
	double wL_ch13;
	
//	double wL_average;
//	double wL_sum;
	//


	// Event detection function.
	// This function is fed with a list of values every time a sample is acquired.
	// Then it checks for specific feature in order to determine if an event occurred.
	private boolean isWinkLeft(List<Double> values) {

		
		// If a recent detection occurred, skip this one.
		if (recentEvent > 0) return false;
		//

		
		// Compare specific channels' values.
		if (Math2.dominantChannel(values) != 1) return false;
		
		wL_ch2 = values.get(1);
		wL_ch4 = values.get(3);
		wL_ch11 = values.get(10);
		wL_ch13 = values.get(12);
		
		if (wL_ch11 > wL_ch2) return false;
		//


		// Raw data LDA calculations.
		double[] wL_testdata1 = { wL_ch2, wL_ch13 }; 
		wL_result = wL_data1.predict(wL_testdata1);
		
		if (wL_result != 1) return false;

		
		double[] wL_testdata2 = { wL_ch2, wL_ch4 };
		wL_result = wL_data2.predict(wL_testdata2);
		
		if (wL_result != 1) return false;
		//

		
		/*
		// Sum and average (excluding channel2).
		for (int x = 0; x < 14; x++) {
			if (x != 1) wL_sum += values.get(x);
		}
		
		if (Math.abs(wL_ch2/wL_sum) < 0.5) return false;
		
		wL_average = wL_sum/13;
		
		if ((wL_ch1/13)/wL_sum < 1) return false;
		//
		*/

		
		/*
		// Output values.
		for (int x = 0; x < 14; x++) {
			System.out.print(values.get(x));
			System.out.print(",");
		}
		System.out.println("");
		//
		*/
		
		
		// Event detected.
		recentEvent = 60;
		return true;
		//
		
		
	}
	//

	
	// Right wink detection.
	// Reference values to feed the LDA algorithm.
	
	// Channels 10 and 13 - values.
	private final double[][] wR_value = {
		
			
			// Trigger event.
			{ 285.0, 0.1 },   { 272.0, 0.05 },   { 248.0, 0.001 }, { 242.0, 0.03 },   { 225.0, 0.1 },
			{ 211.0, 0.08 },  { 207.0, 0.16 },   { 201.0, 0.14 },  { 198.0, -0.05 },  { 197.0, 0.04 },
			{ 196.0, 0.10 },  { 188.0, -0.02 },  { 175.0, 0.09 },  { 172.0, 0.7 },    { 167.0, 0.1 },
			{ 166.0, 0.006 }, { 163.0, -0.07 },  { 161.0, 0.17 },  { 156.0, 0.06 },   { 151.0, 0.12 },
			{ 151.0, 0.01 },  { 146.0, 0.09 },   { 146.0, 0.05 },  { 145.0, 0.0015 }, { 141.0, 0.19 },
			{ 139.0, 0.17 },  { 139.0, -0.006 }, { 138.0, 0.15 },  { 136.0, 0.27 },   { 136.0, 0.14 },
			{ 130.0, 0.13 },  { 128.0, 0.1 },
			//
			
			
			// Baseline detection.
			{ -2.55, 9.6 },   { -3.5, 6.5 },    { -4.34, -0.88 }, { -5.6, 4.8 },    { -6.41, -0.20 },
			{ -7.7, 3.4 },    { -8.6, 0.22 },   { -9.13, 2.7 },   { -10.54, 0.6 },  { -11.2, 3.0 },
			{ -11.7, 0.89 },  { -12.13, 0.45 }, { -13.02, 0.43 }, { -13.16, 0.47 }, { -14.32, 1.86 },
			{ -15.73, 2.28 }, { -16.5, 1.8 },   { -16.7, 0.37 },  { -17.0, 1.28 },  { -17.0, 1.98 },
			{ -17.41, 0.69 }, { -18.0, 0.56 },  { -19.16, 1.57 }, { -22.9, 1.05 },  { -23.18, 1.26 },
			{ -24.34, 1.53 }, { -27.4, 1.14 },  { -29.11, 1.19 }, { -30.2, 1.31 },  { -31.04, 1.14 },
			{ -32.24, 1.19 }, { -33.44, 1.10 }, { -33.5, 1.18 },  { -34.19, 1.22 }, { -35.0, 1.27 },
			//
			
			
			// False detections.
			{ 186.0, 0.9 }, { 126.0, 0.9 }, { 92.0, 0.85 }, { 81.0, 0.9 }
			//
			
			
	};
	//
	
	
	// Channels 10 and 13 - classification.
	private final int[] wR_class = {

			
			// Trigger event.	
			1,1,1,1,1,1,1,1,1,1,	
			1,1,1,1,1,1,1,1,1,1,
			1,1,1,1,1,1,1,1,1,1,
			1,1,
			//
			

			// Baseline detection.
			2,2,2,2,2,2,2,2,2,2,
			2,2,2,2,2,2,2,2,2,2,
			2,2,2,2,2,2,2,2,2,2,
			2,2,2,2,2,
			//

			
			// False detections.
			3,3,3,3
			//
			
			
	};
	//
	
	
	// Channels 11 and 13 - values.
	private final double[][] wR_value2 = {
		
			
			// Trigger event.
			{ 285.1, 0.03 },   { 272.1, -0.013 }, { 248.3, -0.08 },  { 241.9, -0.05 },  { 241.8, -0.003 },
			{ 241.12, 0.004 }, { 225.2, -0.03 },  { 219.9, -0.003 }, { 210.7, 0.008 },  { 206.9, 0.07 },
			{ 206.5, -0.07 },  { 198.15, -0.12 }, { 197.3, -0.007 }, { 188.6, -0.08 },  { 175.1, -0.018 },
			{ 172.6, -0.15 },  { 169.0, 0.004 },  { 167.3, -0.036 }, { 161.3, 0.023 },  { 156.6, 0.023 },
			{ 156.6, 0.023 },  { 151.3, 0.05 },   { 151.12, 0.07 },  { 150.1, 0.055 },  { 148.8, -0.08 },
			{ 146.5, 0.1 },    { 146.5, 0.01 },   { 146.3, 0.085 },  { 141.4, -0.006 }, { 139.1, -0.002 },
			{ 138.8, -0.02 },  { 138.2, 0.03 },   { 138.1, -0.06 },  { 136.1, 0.17 },   { 136.0, -0.05 },
			{ 133.2, 0.04 },   { 132.4, -0.001 }, { 131.2, 0.2 },    { 130.2, -0.06 },  { 129.4, 0.15 },
			{ 127.5, -0.24 },
			//
			
			
			// Initial data filtering.
			{ 706.3, -0.19 }, { 703.8, -0.24 }, { 692.3, -0.28 }, { 656.7, -0.33 }, { 625.2, -0.36 },
			{ 575.7, -0.43 }, { 534.2, -0.50 }, { 515.0, -0.51 }, { 478.6, -0.59 }, { 428.9, -0.7 },
			{ 424.4, -0.64 }, { 410.8, -0.67 }, { 380.0, -0.71 }, { 358.1, -0.79 }, { 345.6, -0.8 }
			//
			

	};
	//


	// Channels 11 and 13 - classification.
	private final int[] wR_class2 = {


			// Trigger event.
			1,1,1,1,1,1,1,1,1,1,	
			1,1,1,1,1,1,1,1,1,1,
			1,1,1,1,1,1,1,1,1,1,
			1,1,1,1,1,1,1,1,1,1,
			1,
			//


			// Initial data filtering.
			2,2,2,2,2,2,2,2,2,2,
			2,2,2,2,2
			//


	};
	//
	
	
	// Classifies stored data.
	LDA wR_data = new LDA(wR_value, wR_class, true);
	LDA wR_data2 = new LDA(wR_value2, wR_class2, true);
	//
	
	
	// Initiate needed variables.
	int wR_result;
	
	double wR_ch2;
	double wR_ch4;
	double wR_ch5;
	double wR_ch13;
	
	double wR_ch10;
	double wR_ch11;
	
	double wR_ch10d13;
	double wR_ch11d13;
	
	double wR_average;
	//
	
	
	// Event detection function.
	private boolean isWinkRight(List<Double> values) {
		
		
		// If a recent detection occurred, skip this one.
		if (recentEvent > 0) return false;
		//
		

		// Comparing channels of interest.
		if (Math2.dominantChannel(values) != 12) return false;
		
		wR_ch2 = values.get(1);
		wR_ch4  = values.get(3);
		wR_ch13 = values.get(12);
		
		if (wR_ch2 > wR_ch13) return false;
		
		wR_ch10 = values.get(9);
		wR_ch11 = values.get(11);
		
		wR_ch10d13 = wR_ch10/wR_ch13;
		wR_ch11d13 = wR_ch11/wR_ch13;
		
		wR_average = Math2.chanRatioAverage(12, values);
		
		if (wR_average < 1) return false;
		//
		
		
		
		// Detect the event by LDA.
		
		
		/*
		double[] wR_testdata = { wR_ch13, wR_ch10d13 };
		wR_result = wR_data.predict(wR_testdata);
		
		if (wR_result != 1) return false;
		*/
		
		
		double[] wR_testdata2 = { wR_ch13, wR_ch11d13 };
		wR_result = wR_data2.predict(wR_testdata2);
		
		if (wR_result != 1) return false;
		//

		
		// Positive detection.
		recentEvent = 60;
		return true;
		//

		
	}
	//
	

	// Getters & Setters.
	public static void setChannel(int x) { 
		channelNumber = x; 
	}

	public static int getChannel() { 
		return channelNumber; 
	}

	public static double getCurrent() {
		return channelValue.get(channelNumber);
	}

	public static boolean getRecording() {
		return recording;
	}

	public static void setRecording(boolean b) {
		recording = b;
	}

	public static void setAverage(int x, double value) {
		channelAverage.set(x,value);
	}

	public static double getAverage(int x) {
		return channelAverage.get(x);
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
	
	public static void setValue(int x, double value) {
		channelValue.add(x, value);
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
	
	
	//
	
	
	//
	Complex[] fftForward 		 = new Complex[128];
	double[] hannWindow 		 = new double[128];
	static double[] magValues	 = new double[128];
	static double[] bands 		 = new double[4];
	//
	
	
	// Removes background from raw EEG data.
	public Processing(List<Double> values) {
		
		
		//
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
			channelValue.set(x, Math2.roundTwo(value - getAverage(x)));

			if (getRecording()) Saving.addRaw(getValue(x), x);
			//
			
				
			/*
			 
			 
			// Register zero crossing.
			if ((last > 0 && channelValue.get(x) < 0) || (last < 0 && channelValue.get(x) > 0)) 
			{
				zeroCross.set(x, zeroCross.get(x)+1);
			}
			//
			
			
			// Save last recorded value.
			setLast(x, getValue(x));
			//
			 

			 */
			
			
		}
		//		
		
		
		// Adds new values to the EEG chart.
		if (VISUALIZE && VISUALIZE_EEG) EegChart.addEeg(getCurrent());
		//
		
		
		// Avoid detecting too many events.
		if (recentEvent > 0) recentEvent -= 1;
		//
		
		
		// Winks detection.
		if (detectWL && isWinkLeft(getValue())) {
			System.out.println("WINK LEFT");
		}
		
		
		if (detectWR && isWinkRight(getValue())) {
			System.out.println("WINK RIGHT");
		}
		//

		
		// Adds values to a sample buffer to perform FFT.
		Fft.addList(getValue());
		//
		

		// Display each channel's activity on a square graphic.
		if (VISUALIZE && VISUALIZE_SQUARE) {
			Square square = Interface.square;
			square.squareRepaint(channelValue);
		}
		//
		
		
		// If enough samples have been recorder, perform FFT.
		if (Fft.listSize() == 128 && calcFft) {
			
			
			// Perform FFT algorithm.
			magValues  = Fft.calcMagnitude(getChannel());
			
			if (VISUALIZE && VISUALIZE_FFT) Fft.displayFft(magValues);
			//
			
			
			// Separates frequencies in brain bands.
			bands = Bands.calcBands(magValues);
			
			if (VISUALIZE && VISUALIZE_BANDS) Bands.displayBands(bands);
			//
			
			
		}
	}
}
