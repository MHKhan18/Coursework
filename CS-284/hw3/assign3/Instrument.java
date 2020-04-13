/*
 * @author Mohammad Khan
 * Instrument.java
 */
package assign3;

public abstract class Instrument {
    
    protected  InstString[] strings;
    
    protected  double CONCERT_A = 440.0;
    
    public void playNote(int i){
    	
    	strings[i].pluck();

    }

    public  double ringNotes(){
    	
    	
    	double result=0;
    	for (InstString string : strings) {
    		
    		result += string.sample();
    		string.tic();
    	}
    	
    	return result;

    }

}
