package main;

import java.util.Arrays;
import java.util.List;

public class Classify {

	private static double[][] createData(String event, int channel1, int channel2) {
		
		String[] c1 = Saving.returnData(event, channel1);
		String[] c2 = Saving.returnData(event, channel2);
		
		String[] b1 = Saving.returnData("baseline", channel1);
		String[] b2 = Saving.returnData("baseline", channel2);
		
		double[][] data = new double[c1.length+b1.length][2];

		for (int x = 0; x < c1.length; x++) {
			data[x][0] = Double.valueOf(c1[x]);
			data[x][1] = Double.valueOf(c2[x]);
		}

		for (int y = 0; y < b1.length; y++) {
			data[y+c1.length][0] = Double.valueOf(b1[y]);
			data[y+c1.length][1] = Double.valueOf(b2[y]);
		}

		return data;
	}

	
	private static double[][] createData2(String event) {
		
		String[] c = Saving.returnData(event, 3);
		String[] b = Saving.returnData("baseline", 3);
		double[][] data = new double[c.length+b.length][14];
		
		for (int x = 0; x < 14; x++) {
			
			String[] c1 = Saving.returnData(event,x);
			
			for (int y = 0; y < c.length; y++) {
				
				data[y][x] = Double.valueOf(c1[y]);
				
				
			}
		}
		
		for (int x = 0; x < 14; x++) {
			
			String[] b1 = Saving.returnData("baseline", x);
			
			for (int y = 0; y < b1.length; y++) {
				
				data[y+c.length][x] = Double.valueOf(b1[y]);
			}
		}
		
		return data;
		
	}
	
	
	private static int[] dataClass(String event, int channel1) {
		
		String[] c1 = Saving.returnData(event, channel1);
		
		String[] b1 = Saving.returnData("baseline", channel1);
		
		
		int[] one = new int[c1.length];
		Arrays.fill(one, 0);
		
		int[] two = new int[b1.length];
		Arrays.fill(two, 1);
		
		int[] both = new int[one.length+two.length];
		System.arraycopy(one, 0, both, 0, one.length);
		System.arraycopy(two, 0,  both, one.length, two.length);
		
		return both;
	}
	
	
	private static double[][] blinkData;
	private static double[][] blinkDataFull;
	
	private static int[] blinkClass;
	
	private static LDA blinkLDA;
	private static LDA blinkLDA2;
	
	private static double[] blinkTest;
	
	public static double[][] getBlinkData() {
		return blinkData;
	}
	
	public static int[] getBlinkClass() {
		return blinkClass;
	}
	
	public static int predict(double[] data, int x) {
		
		if (x == 1) return blinkLDA.predict(data);
		else return blinkLDA2.predict(data);
		
	}
	
	
	// FUNÇÃO PARA CRIAR UM ARRAY COM DADOS DE TODOS OS CANAIS
	public static double[] setBlinkTest(List<Double> values) {
		
		blinkTest = new double[14];
		
		for (int x = 0; x < 14; x++) {
			
			blinkTest[x] = values.get(x+3);
			
		}
		
		return blinkTest;
		
	}
	
	public Classify() {
		
		double[][] blinkData = createData("blink", 0, 1);
		int[] blinkClass = dataClass("blink", 1);
		
		double[][] blinkDataFull = createData2("blink");

		blinkLDA = new LDA(blinkData, blinkClass, true);
		
		blinkData = createData("blink", 11, 12);
		blinkClass = dataClass("blink", 11);
		
		blinkLDA2 = new LDA(blinkData, blinkClass, true);
	}
	
	
}
