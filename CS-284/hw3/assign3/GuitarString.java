/*
 * @suthor Mohammad Khan
 * GuitarString.java
 */

package assign3;
import java.lang.Math;

public class GuitarString extends InstString{

    public GuitarString(){};
   
 
    public GuitarString(double frequency) {	
    	
    	int N = (int)Math.round(SAMPLING_RATE/frequency);
    	vibrations = new RingBuffer(N);
    	
    	for (int i=0; i<N; i++) {
    		vibrations.enqueue(0);
    	}

    }

    public GuitarString(double[] init) {
    	
    	int N = init.length;
    	vibrations = new RingBuffer(N);
    	
    	for (int i=0; i<N; i++) {
    		vibrations.enqueue(init[i]);
    	}
    	

    }
   
    public void pluck() {	
    	
    	while (vibrations.getSize()>0) {
    		vibrations.dequeue();
    	}
    	
    	while (!vibrations.isFull()) {
    		vibrations.enqueue(Math.random()-0.5);
    	}
    	
    	
	
    }
   
    public void tic() {
    	
    	double val1 = vibrations.dequeue();
    	double val2 = vibrations.peek();
    	
    	double newVal =  ((val1+val2)/2.0)*DECAYING_FACTOR;
    	
    	vibrations.enqueue(newVal);
    	
    	tick_count++;
    	
	
    }
    
    public static void main ( String [] args ) {
    	int N = 25 ;
    	double [ ] samples = { .2 , .4 , .5 , .3 , -.2 , .4 , .3 , .0 , -.1 , -.3 };
    	GuitarString testString = new GuitarString ( samples ) ;
    	for ( int i = 0 ; i < N; i ++) {
    		int t = testString.time ( ) ;
    		double sample = testString.sample( ) ;
    		System.out. printf ( "%6d %8.4f\n", t , sample ) ;
    		testString.tic( ) ;
    		}
    	}

}
