/*
 * @author Mohammad Khan
 * PianoString.java
 *
 */

package assign3;
import java.lang.Math;

public class PianoString extends InstString {
  
    public PianoString(double frequency) {
    	int N = (int)Math.round(SAMPLING_RATE/frequency);
    	vibrations = new RingBuffer(N);
    	
    	for (int i=0; i<N; i++) {
    		vibrations.enqueue(0);
    	}

    }

    public PianoString(double[] init) {
    	int N = init.length;
    	vibrations = new RingBuffer(N);
    	
    	for (int i=0; i<N; i++) {
    		vibrations.enqueue(init[i]);
    	}

    }
   
    public void pluck() {
    	
    	int N = vibrations.getSize();
    	
    	while (vibrations.getSize()>0) {
    		vibrations.dequeue();
    	}
    	
    	for (int i=0; i<N; i++) {
    		if ( i < ((7.0/16)*N) || i > ((9.0/16)*N)) {
    			vibrations.enqueue(0);
    		}
    		else {
    			vibrations.enqueue(0.25*Math.sin(8*(Math.PI)*((i/(double)N)-(7.0/16))));
    		}
    	}
    	
    	
    }
   
    public void tic() {	
    	double val1 = vibrations.dequeue();
    	double val2 = vibrations.peek();
    	
    	double newVal =  ((val1+val2)/2.0)*DECAYING_FACTOR;
    	
    	vibrations.enqueue(newVal);
    	
    	tick_count++;

    }

}
