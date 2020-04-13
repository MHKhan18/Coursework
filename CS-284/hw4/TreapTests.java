package hw4;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

public class TreapTests {
	 @Test
	    @DisplayName("Testing toString() of treap")
	    void test() {
		 
		 Treap<Integer> testTree = new Treap < Integer >();
	    	testTree.add (4 ,81);
	    	testTree.add (2 ,69);
	    	testTree.add (6 ,30);
	    	testTree.add (1 ,16);
	    	testTree.add (3 ,88);
	    	testTree.add (5 ,17);
	    	testTree.add (7 ,74);
	    	
	    	String testTreeString = testTree.toString();
	    	
	    	String result = testTreeString.replaceAll("[\\n\\t ]", "").toLowerCase(); // to replace all space and new line characters and maintain uniform case
	    	
	    	String expected = "(key=1,priority=16)null(key=5,priority=17)(key=2,priority=69)null(key=4,priority=81)(key=3,priority=88)nullnullnull(key=6,priority=30)null(key=7,priority=74)nullnull";
	    	
	    	assertEquals(expected,result);
	    	 
		 
		 
	 }

}
