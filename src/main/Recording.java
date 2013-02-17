package main;

import java.util.ArrayList;
import java.util.List;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;

import emotiv.Edk;

public class Recording {

	
	// Initiate objects.
	private Pointer 		dataHandle 	 = Edk.INSTANCE.EE_DataCreate();
	private IntByReference samplesTaken = new IntByReference(0);
	
	private List<Double> 	samples 	 = new ArrayList<Double>();
	//
	
	
	// Main class.
	public Recording() {
		
		// Updates the data handle with new data since the last call.
		Edk.INSTANCE.EE_DataUpdateHandle(0, dataHandle);
		//
		

		// Detects and stores in 'samplesTaken' the number of samples in the data handle.
		Edk.INSTANCE.EE_DataGetNumberOfSample(dataHandle, samplesTaken);
		//
		
		
		// Detects if there are any new samples.
		if (samplesTaken != null && samplesTaken.getValue() != 0) {
			
			// Creates an array with enough capacity for the new samples.
			double[] data = new double[samplesTaken.getValue()];
			//
			
			
			// Loops through all new recorded samples.
			for (int sampleId = 0; sampleId < samplesTaken.getValue(); ++sampleId) {
				
				for (int x = 3; x < 17; x++) {
					Edk.INSTANCE.EE_DataGet(dataHandle, x, data, samplesTaken.getValue());
					samples.add(data[sampleId]);
				}
				
				/*
				for (int x = 0; x < 17; x++) {
					Edk.INSTANCE.EE_DataGet(dataHandle, x, data, samplesTaken.getValue());
					samples.add(data[sampleId]);
				}
				*/
			//
				
				// Process samples.
				new Processing(samples);
				samples.clear();
			}
		}
	}
}
