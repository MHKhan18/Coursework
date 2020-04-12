/**
 * 
 * @author Mohammad Khan

 * username: mkhan13
 * Pledge: "I pledge my honor that I have abided by Stevens Honor System."
 */



/**
 *Represents a rational number 
 *and a few operations on them
 */

public class RationalNumber extends Number {
	
	//Data Fields
	
	private int p; //numerator
	private int q; //denominator
	
	//Methods
	
	//Basic Operations 
	
	/**
	 * constructs a rational number 
	 *@param num the numerator
	 * @param den the denominator
	 * @throws ArithmeticException when den is 0
	 */
	public RationalNumber(int num, int den) {
		
		if (den == 0) {
			throw new ArithmeticException("Denominator can not be zero.");
		}
		
		p = num ;
		q = den ;
		
	}
	
	
	/**Simplifies the rational object
	 * @return the simplest form of the rational number 
	 */
	
	public void simplify() {
		
		int r; // simplified numerator
		int s; // simplified denominator
		int tempR; //temporarily holds r during gcd calculation
		int sign; // holds the sign of the rational number
		
		if (p == 0) {
			q = 1;
		}
		
		else {
			if (p*q < 0) {
				sign = -1;
			}
			else {
				sign = 1;
			}
			
			r = Math.abs(p);
			s = Math.abs(q);
			
			while (r%s != 0) {
				tempR = r;
				r = s;
				s = tempR % s ;
			}
			
			p = (Math.abs(p)/s)*sign ;
			q = Math.abs(q)/s ;
		}
		
	}
	
	/** 
	 * adds up increment to this object 
	 * @param increment the value added to this 
	 */
	
	public void add (RationalNumber increment) {
		
		int r = increment.p ;
		int s = increment.q ;
		
		p = (p*s) + (r*q) ;
		q = q*s ;
		
		RationalNumber intermediate = new RationalNumber(p,q) ;
		
		intermediate.simplify();
		
		p = intermediate.p;
		q = intermediate.q;
		
	}
	
	/**
	 * multiplies this rational number by factor 
	 * @param factor multiplies this 
	 */
	
	public void multiply (RationalNumber factor) {
		
		p = p * factor.p ;
		q = q * factor.q ;
		
		RationalNumber intermediate = new RationalNumber(p,q) ;
		
		intermediate.simplify();
		
		p = intermediate.p;
		q = intermediate.q;
		
	}
	
	// implementing java.lang.Number functions
	
	/**
	 * @return a double which represents the rational number in
      * decimal form upto 3 decimal places(truncate if the result 
      * has more than 3 decimal places).
	 */

	@Override 
	public double doubleValue() {
		
		double decimalForm = (double)p/q ; 
		
		double truncateForm = Math.floor(Math.abs(decimalForm)*1000)/1000 ; 
		
		if (decimalForm < 0) {
			return -1*truncateForm ;
		}
		else {
			return truncateForm ;
		}
		
	}
	
	/**
	 * @return a float which represents the rational number in
      * decimal form upto 3 decimal places(truncate if the result 
      * has more than 3 decimal places).
	 */
	@Override 
	public float floatValue() {
		
		float decimalForm = (float)p/q ; 
		
		double truncateForm = Math.floor(Math.abs(decimalForm)*1000)/1000 ; 
		
		if (decimalForm < 0) {
			return -1*(float)truncateForm ;
		}
		else {
			return (float)truncateForm ;
		}
		
	}
	
	/**
	 * @return an int which represents the integer part of the decimal 
	 * representation of the rational number.
	 */
	@Override 
	public int intValue() {
		int decimalForm = p/q ;
		return decimalForm ;
	}
	
	/**
	 * @return an long which represents the integer part of the decimal 
	 * representation of the rational number.
	 */
	@Override 
    public long longValue() {
		long  decimalForm = (long)p/(long)q ;
		return decimalForm ;
	}
	
	//Binary Periodic Number
	
	/**
	 * helper method
	 * converts binary representation of a number to decimal form
	 * @param str the binary string
	 * @return integer form 
	 */
	
	private int binaryToDecimal(String str) {
		
		int result = 0;
		
		for(int i = 0; i < str.length(); i++) {
			if(str.charAt(i)=='1') {
				result += Math.pow(2,str.length()-1-i);
			}
		}
		
		return result;
		
	}
	
	/**
	 * constructs a RationalNumber object
	 * @param s the binary periodic number
	 */
	
	public RationalNumber(String s) {
		
		int apIndex = s.indexOf('.');
		int pIndex = s.indexOf('_');
		
		String characteristic;
		String antiPeriod; 
		String period;
		
		if (apIndex == -1 && s != "" ) {
			characteristic = s;
			}
		else {
			characteristic = s.substring(0,apIndex);
		}
		
		if (pIndex == -1) {
			antiPeriod = s.substring(apIndex+1);
			period = "";
		}
		else {
			antiPeriod = s.substring(apIndex+1,pIndex);
		    period = s.substring(pIndex+1);
		}
			
		if (apIndex == -1 && pIndex == -1 ) {
			characteristic = s;
			antiPeriod = "";
			period = "";
		}
		if (characteristic=="" && antiPeriod == "" && period == "" || s=="._") {
			throw new IllegalArgumentException("Value can not be empty");
		}
		
		
		
		Integer charDec = binaryToDecimal(characteristic);
		Integer apDec = binaryToDecimal(antiPeriod);
		Integer pDec = binaryToDecimal(period);
		
		//System.out.println(charDec);
		
		int pLen = period.length();
		int apLen = antiPeriod.length();
		
		
		
		
		RationalNumber charVal = new RationalNumber(charDec,1);
		RationalNumber apVal = new RationalNumber(apDec, (int)Math.pow(2, apLen));
		RationalNumber pVal = new RationalNumber(pDec, (int)((Math.max((Math.pow(2, pLen)-1), 1)) * Math.pow(2, apLen)));
		
		
		
		
		charVal.add(apVal);
		charVal.add(pVal);
		charVal.simplify();
		
		p = charVal.p;
		q = charVal.q;
		
		
	}
	
	/**
	 * @return fraction that represents the object 
	 */
	
	
	@Override 
	public String toString() {
		return p + "/" + q;
	}
	
	
	
}
