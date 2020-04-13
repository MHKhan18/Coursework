/*
 * @author Mohammad Khan
 * Piano.java
 */

package assign3;


public class Piano extends Instrument{

    private static InstString[][] pStrings; //2D array of strings
    
    public Piano(int numNotes){
    	
    	pStrings = new InstString[numNotes][3];
    	
    	for (int i=0; i<numNotes; i++) {
    		pStrings[i][0] =  new PianoString((CONCERT_A * (Math.pow(2,((i-24)/12.0))))-0.45);
    		pStrings[i][1] =  new PianoString((CONCERT_A * (Math.pow(2,((i-24)/12.0)))));
    		pStrings[i][2] =  new PianoString((CONCERT_A * (Math.pow(2,((i-24)/12.0))))+0.45);
    	}

    }
  
    public void playNote(int i){
    	pStrings[i][0].pluck();
    	pStrings[i][1].pluck();
    	pStrings[i][2].pluck();
   	 
    }
    
    public double ringNotes(){
    	double result=0;
    	for (InstString[] arr_string : pStrings) {
    		for (int i=0; i<arr_string.length; i++) {
	    		arr_string[i].tic();
	    		result += arr_string[i].sample();
	    		
    		}
    		
    	}
    	
    	return result;


    }
			    

}
