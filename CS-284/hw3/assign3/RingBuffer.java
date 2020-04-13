/*
 * RingBuffer.java
 *
 */

package assign3;

/** 
 * Creates a buffer using cyclic wrap-around
 *
 * @author Justin Barish
 * @version 1.0
 * @since 2015-10-26
 *
 */
public class RingBuffer {
    private int first = 0;            // index of first item in buffer
    private int last = 0;             // index of last item in buffer
    private int size = 0;             // current number of items of buffer
    private double[] buffer;

    /**
     * create an empty buffer, with given max capacity
     *
     * @param capacity  maximum capacity of the buffer
     *
     */
    public RingBuffer(int capacity) {
        buffer = new double[capacity];
    }

    /**
     * gets number of items currently in the buffer
     *
     * @return  number of elements in the buffer
     *
     */
    public int getSize() {
    	return size;
    }

    /**
     * cheks if buffer is empty
     *
     * @return  true if empty, false if not empty
     *
     */
    public boolean isEmpty() {
       	return (size == 0 ? true :false);
    }

    /** 
     * is the buffer full (size equals array capacity)?
     * 
     * @return true if buffer is filled, false if not filled
     *
     */
    public boolean isFull() {
        return (size >= buffer.length? true :false);
    }

    /**
     * add item x to the end
     *
     * @param x element to add to end of buffer
     *
     */
    public void enqueue(double x) {
        if (isFull()) { throw new RuntimeException("Ring buffer overflow"); }
	
	buffer[last] = x; 
	
	if (last != buffer.length-1){ // as long as last is not at the end of the buffer,
	    last ++;                  // increment it.
	} 
	else {last = 0;}              // otherwise, set it back to the beginning of the buffer.
	size ++;
    }

    /** 
     * delete and return item from the front
     *
     * @return value at front of buffer
     *
     */
    public double dequeue() {
        if (isEmpty()) { throw new RuntimeException("Ring buffer underflow"); }

	double numToDequeue = buffer[first];

	/* increments first as long as it is not at end of buffer; otherwise sets first to zero index. 
	 * by incrementing first, reference to dequeued element is removed, not the actual value.
	 * The element will be overwritten when the buffer wraps back around to that index. */
	
      	if (first != buffer.length-1){first ++;} 
      		else {first = 0;}
	
		size--;
		return numToDequeue;
		
    }

    /** 
     * return (but do not delete) item from the front
     *
     * @return value at front of buffer
     *
     */
    public double peek() {
        if (isEmpty()) { throw new RuntimeException("Ring buffer underflow"); }

        return buffer[first];
    }
}


