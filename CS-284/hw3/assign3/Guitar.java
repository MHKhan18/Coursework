/*
 * @author Mohammad Khan
 * Guitar.java
 */
package assign3;

public class Guitar extends Instrument{


    public Guitar(int numNotes){
    	
    	strings = new InstString[numNotes];
    	
    	for (int i=0; i<numNotes; i++) {
    		strings[i] =  new GuitarString(CONCERT_A * (Math.pow(2,((i-24)/12.0))));
    	}

    }		    
}
