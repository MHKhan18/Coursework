/*
 * RockBand.java
 *
 */

package assign3;
import cos126.StdDraw;
import cos126.StdAudio;
import java.io.IOException;
import java.util.ArrayList;

public class RockBand {


    private static String guitarBassKeyboard ="`1234567890-=qwertyuiop[]\\asdfghjkl;'";
    private static String pianoKeyboard = "~!@#$%^&*()_+QWERTYUIOP{}|ASDFGHJKL:\"";
    private static String drumKeyboard = "ZXCVBNM<>?zxcvbnm,.";
  
    public static void main(String[] args) {
    	
    	Instrument[] instList = {new Guitar(37),new Piano(37), new Bass(37), new Drum(19)};
    	
    	int enter_count = 0;
    	boolean LowMode = false;
    	
    	
    	
    	while (true) {
    		double sumOfAllInstrumentSamples = 0;
            // check if the user has typed a key; if so, process it   
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                
                if (key == '\n') {
                	enter_count++;
                	
                	if (enter_count%2 != 0 ) {
                		LowMode = true;
                	}
                	else {
                		LowMode = false;
                	}
                }
                
                if (guitarBassKeyboard.indexOf(key) != -1) {
                	
                	int index = guitarBassKeyboard.indexOf(key);
                	if (LowMode) {
                		instList[2].playNote(index);
                		//sumOfAllInstrumentSamples += instList[2].ringNotes();
                	}
                	else {
                		instList[0].playNote(index);
                		//sumOfAllInstrumentSamples += instList[0].ringNotes();
                		
                	}
                	
                }
                
                else if (pianoKeyboard.indexOf(key) != -1) {
                	
                	int index = pianoKeyboard.indexOf(key);
                	
                	instList[1].playNote(index);
                	
                	//sumOfAllInstrumentSamples += instList[1].ringNotes();
                }
                	
                else if (drumKeyboard.indexOf(key) != -1) {
                	
                	int index = drumKeyboard.indexOf(key);
                	
                	instList[3].playNote(index);
                	
                	//sumOfAllInstrumentSamples += instList[3].ringNotes();
                }
            }
            
          
            for (Instrument instrument : instList) {
            	sumOfAllInstrumentSamples += instrument.ringNotes();
            }
            
            
            
            StdAudio.play(sumOfAllInstrumentSamples);
            
            
    }
    
    }
    
}
