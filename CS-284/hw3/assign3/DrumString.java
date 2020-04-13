/*
 * @author Mohammad Khan
 * DrumString.java
 */

package assign3;
import java.lang.Math;


public class DrumString extends InstString{

   
    public DrumString(double frequency) {
    	int N = (int)Math.round(SAMPLING_RATE/frequency);
    	vibrations = new RingBuffer(N);
    	
    	for (int i=0; i<N; i++) {
    		vibrations.enqueue(0);
    	}

    }

    public DrumString(double[] init) {
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
    		if (Math.sin(1.0/N)>0) { //confusion
    			vibrations.enqueue(1);
    		}
    		else {
    			vibrations.enqueue(-1);
    		}
    	}
    	
    }
    
    public void tic() {
    	
    	double val1 = vibrations.dequeue();
    	double val2 = vibrations.peek();
    	
    	double newVal =  ((val1+val2)/2.0)*DECAYING_FACTOR;
    	
    	
    	
    	double randVal = Math.random();
    	double randSign = Math.random();
    	
    	if (randVal<0.6) {
    		if (randSign < 0.5) {
    			vibrations.enqueue(newVal);
    		}
    		else {
    			vibrations.enqueue(-1*newVal);
    		}
    		
    	}
    	else {
    		if (randSign < 0.5) {
    			vibrations.enqueue(val1);
    		}
    		else {
    			vibrations.enqueue(val1);
    		}
    		
    	}
    	
	
    }
}
