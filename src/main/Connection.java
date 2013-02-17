package main;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;

import emotiv.Edk;
import emotiv.EdkErrorCode;
import emotiv.EmoState;

public class Connection {

	
	//
	private float 		BUFFER_SIZE	 = 1;
	private boolean 	emoRecording = false;
	//
	
	
	// Connect to EmoEngine.
	public Connection() {
		
		// Handle to hold EmoEngine events (ex: EE_UserAdded, EE_EmoStateUpdated,
		// EE_CognitivEvent).
		Pointer emoEvent = Edk.INSTANCE.EE_EmoEngineEventCreate();

		// Handle to store EmoStates.
		Pointer emoState = Edk.INSTANCE.EE_EmoStateCreate();

		// Declare variables to store the current user and the detected samples.
		IntByReference userID = new IntByReference(0);

		// Declare variable to store error messages in int.
		int state = 0;

		// Connect to the EmoComposer. Terminate the program if the connection fails.
		if (Edk.INSTANCE.EE_EngineConnect("Emotiv Systems-5") != EdkErrorCode.EDK_OK.ToInt()) {
			System.out.println("Start up failed.");
			return;
		}

		// Sets the size of the data buffer. Defines how frequent EE_DataUpdateHandle()
		// needs to be called to prevent data loss.
		Edk.INSTANCE.EE_DataSetBufferSizeInSec(BUFFER_SIZE);

		
		//
		//new Classify();
		//
		
		
		// Detection loop. Will loop until terminated or if an error occurs.
		while (true) {

			// Detects if there is a new event. Returns EDK_OK if there is a new event.
			// Stores the event in the variable 'emoEvent'.
			state = Edk.INSTANCE.EE_EngineGetNextEvent(emoEvent);

			
			Edk.INSTANCE.EE_EmoEngineEventGetEmoState(emoEvent, emoState);
			
			/*
			if(EmoState.INSTANCE.ES_ExpressivIsBlink(emoState) == 1) {
				System.out.println("BLINK");
			}
			*/
			
			// If there is a new event.
			if (state == EdkErrorCode.EDK_OK.ToInt()) {

				// Detects the type of event stored in 'emoEvent'.
				int eventType = Edk.INSTANCE.EE_EmoEngineEventGetType(emoEvent);

				// Detects if a new user was added with the current event.
				// 'userID' will store the new user's id.
				Edk.INSTANCE.EE_EmoEngineEventGetUserId(emoEvent,  userID);

				// Determines if the current event added a new user and if the detection
				// of the new user was successful.
				if (eventType == Edk.EE_Event_t.EE_UserAdded.ToInt() && userID != null) {
					
					// Initiates the EEG recording for the current user id.
					// Sets the 'emoRecording' variable to true, initiating the recording loop.
					Edk.INSTANCE.EE_DataAcquisitionEnable(userID.getValue(), true);
					emoRecording = true;
				}
			}

			// If the state is not EDK_OK and not EDK_NO_EVENT, there is a problem.
			// Prints the current state and breaks the loop.
			else if (state != EdkErrorCode.EDK_NO_EVENT.ToInt()) {
				System.out.print("EmoEngine error: ");
				System.out.println(state);
				break;
			}

			// If the EEG recording was initiated.
			if (emoRecording == true) {
				new Recording();
			}
		}
		//







		// DISCONNECT

		// Disconnects from the EmoEngine and frees the used handles.
		Edk.INSTANCE.EE_EngineDisconnect();
		Edk.INSTANCE.EE_EmoStateFree(emoState);
		Edk.INSTANCE.EE_EmoEngineEventFree(emoEvent);
		System.out.println("Disconnected from EmoEngine.");
		//

	}

}


