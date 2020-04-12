/**
 * @author Mohammad Khan
 */

 public class TestRationalNumber {
	/**
	 * this method was used for testing during development 
	 * @param args
	 */
	
	public static void main (String[] args) {
		//System.out.println("Hey there!!");
		
		RationalNumber t16 = new RationalNumber(5,30);
		t16.simplify();
		System.out.println(t16);
		
		
		RationalNumber t15 = new RationalNumber(".10");
		System.out.println(t15);
		
		RationalNumber t13 = new RationalNumber(-5,31);
		System.out.println(t13);
		t15.add(t13);
		System.out.println(t15);
		
		
		
		RationalNumber t14 = new RationalNumber(-8,31);
		t14.multiply(t13);
		System.out.println(t14);
		RationalNumber t1 = new RationalNumber(12,24);
		
		t1.simplify();
		System.out.println(t1);
		//System.out.println(t1.q);
		
		System.out.println(t1.doubleValue());
		
		//RationalNumber t2 = new RationalNumber(12,0);
		//System.out.println(t2.doubleValue());
		
		RationalNumber t3 = new RationalNumber(1,3);
		System.out.println(t3.doubleValue());
		
		t3.add(t1);
		System.out.println(t3.doubleValue());
		
		t3.multiply(t1);
		System.out.println(t3.doubleValue());
		System.out.println(t3.floatValue());
		System.out.println(t3.intValue());
		System.out.println(t3.longValue());
		System.out.println(t3);
		
		
		
		//System.out.println(binaryToDecimal("110"));
		
		RationalNumber t4 = new RationalNumber("110.10_111");
		System.out.println(t4);
		
		RationalNumber t5 = new RationalNumber(".10_001");
		System.out.println(t5);
		
		
		RationalNumber t6 = new RationalNumber("10.1");
		System.out.println(t6);
		
		
		RationalNumber t7 = new RationalNumber("10._101");
		System.out.println(t7);
		
		RationalNumber t8 = new RationalNumber("._01");
		System.out.println(t8);
		
		
		RationalNumber t9 = new RationalNumber("100.01_101");
		System.out.println(t9);
		
		RationalNumber t10 = new RationalNumber("100");
		System.out.println(t10);
		
		//RationalNumber t11 = new RationalNumber("._");
		//System.out.println(t11);
		//System.out.println(t11.q);
		
		t9.multiply(t10);
		System.out.println(t9);
		
		
		t7.add(t8);
		System.out.println(t7);
		
		System.out.println(t7.doubleValue());
		System.out.println(t7.floatValue());
		System.out.println(t7.intValue());
		System.out.println(t7.longValue());
		t14.add(t13);
		System.out.println(t13);
		
	}
	
}
 
	
	
	
	
	
