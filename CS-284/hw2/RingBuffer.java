/**
 * 
 * @author Mohammad Khan
 * username: mkhan13
 * CS-284-D
 * " I pledge my honor that I have abided by the Stevens Honor System."
 *
 */

 /*
 * a class representing a RingBuffer 
 */
public class RingBuffer {
    private int first;            // index of first item in buffer
    private int last;             // index of last item in buffer
    private int size;             // current number of items of buffer
    private int capacity;         // max storage of buffer
    private double[] buffer;	  // array to hold buffer data 

    /*
     * constructor for an empty buffer, with given max capacity
     */
    public RingBuffer(int capacity) {
        this.first = 0;
        this.last = 0;
        this.size = 0;
        this.capacity = capacity;
        this.buffer = new double[capacity];
        
    }

    /*
     * @return an int representing  number of items currently in the buffer
     */
    public int getSize() {
        return size;
    }

    /**
     * @return a boolean stating whether the buffer is empty 
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * @return a boolean stating whether the buffer is full
     */
    public boolean isFull() {
    	return size == capacity ; 
       
    }

    /**
     * 
     * @param  double x to be added to the tail of the buffer 
     */
    public void enqueue(double x) {
        if (isFull()) {
        	throw new RuntimeException("Ring buffer overflow"); 
        	}
        
        else {
        	buffer[last] = x;
        	last = (last+1)%capacity ;
        	size++;
        }
        
    }

    /**
     * 
     * @return head of the buffer 
     * deletes the head 
     */
    public double dequeue() {
        if (isEmpty()) { throw new RuntimeException("Ring buffer underflow"); }
        else {
        	double ans = buffer[first];
        	first = (first+1) % capacity ; 
        	size--;
        	return ans;
        }
    }

    /**
     * 
     * @return  (but do not delete) item from the head 
     */
    public double peek() {
        if (isEmpty()) { throw new RuntimeException("Ring buffer underflow"); }
        else {
        	return buffer[first];
        }
    }

    // a simple test of the constructor and methods in RingBuffer
    public static void main(String[] args) {
        int N = Integer.parseInt(args[0]);
        RingBuffer buffer = new RingBuffer(N);
        for (int i = 1; i <= N; i++) {
            buffer.enqueue(i);
        }
		double t = buffer.dequeue();
		buffer.enqueue(t);
		System.out.println("Size after wrap-around is " 
				   + buffer.getSize());
        while (buffer.getSize() >= 2) {
            double x = buffer.dequeue();
            double y = buffer.dequeue();
            buffer.enqueue(x + y);
        }
        System.out.println(buffer.peek());
    }
}



