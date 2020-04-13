/*
 * 
 * InstString.java
 */

package assign3;

public abstract class InstString{
	
	
	protected final int SAMPLING_RATE = 44100;
	protected final double DECAYING_FACTOR = 0.996; 
	protected RingBuffer vibrations;
	
	protected int tick_count = 0;
	

    /* To be implemented by subclasses*/
    public abstract void pluck();
    public abstract void tic();

    public double sample(){
    	return vibrations.peek();

    }

    public int time(){
    	
    	return tick_count;
    }

}
