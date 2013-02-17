package main;

public class Smooth extends Thread {

	public Smooth(String band, double value) throws InterruptedException {
		
		BarChart.addValue(band, value/5);
		sleep(500);
		BarChart.addValue(band, value/4);
		sleep(250);
		BarChart.addValue(band, value/3);
		sleep(150);
		BarChart.addValue(band, value/2);
		sleep(100);
		BarChart.addValue(band,  value);
	}

}
