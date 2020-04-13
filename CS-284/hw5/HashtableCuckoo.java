package hw5;

import java.util.AbstractMap;
import java.util.*;

/**
 * @author Mohammad Khan
 * Implements hash table with Cuckoo Hashing
 * @param <K> key for hashtable
 * @param <V> value for hashtable
 */

public class HashtableCuckoo<K, V> implements KWHashMap<K, V> {

    
	// Data fields
    private Entry<K, V>[] table1;
    private Entry<K, V>[] table2;
    private List<Entry<K, V> > overflow;
    private static final int START_CAPACITY = 100;
    private double LOAD_THRESHOLD = 0.6;
    private int tableSize= (int) (START_CAPACITY*LOAD_THRESHOLD);
    private int numKeys;
 
    
    
    // constructor
    public HashtableCuckoo() {
    	
    	table1 = new Entry[tableSize];
    	table2 = new Entry[tableSize];
    	
    	int stashCapacity = (int)(Math.log((double)START_CAPACITY)/Math.log(2));
    	overflow = new ArrayList<Entry<K,V>>(stashCapacity);
    	
	
    }
  
    /** Contains key-value pairs for a hash table. */
    public static class Entry<K, V> implements Map.Entry<K, V> {

        /** The key */
        private K key;
        /** The value */
        private V value;

        /**
         * Creates a new key-value pair.
         * @param key The key
         * @param value The value
         */
        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        /**
         * Retrieves the key.
         * @return The key
         */
        @Override
        public K getKey() {
            return key;
        }

        /**
         * Retrieves the value.
         * @return The value
         */
        @Override
        public V getValue() {
            return value;
        }

        /**
         * Sets the value.
         * @param val The new value
         * @return The old value
         */
        @Override
        public V setValue(V val) {
            V oldVal = value;
            value = val;
            return oldVal;
        }

	/**
         * Return a String representation of the Entry
         * @return a String representation of the Entry
         *         in the form key = value
         */
        @Override
        public String toString() {
            return key.toString() + "," + value.toString();
        }
        
    }

    
    /*
     * @return int the number of objects currently in the hash table
     * */
    @Override
    public int size() {
    	return numKeys;
    }
    
    /*
     * @ return boolean true if the hashTable is empty, false otherwise
     */

    @Override
    public boolean isEmpty() {
    	return numKeys == 0;
    }
    
    /*
     * @param object key 
     * @param int indicate which hashTable 
     * @return int the index of the key in hashTable
     */
    private int getHash(Object o, int num){
    	
    	int hashCode = o.hashCode();
    	
    	if ( num == 0) {
    		
    		hashCode = hashCode % tableSize;  // hash function for table 1
        	
        	if (hashCode < 0) {
        		hashCode += tableSize;
        	}
    		return hashCode;
    	}
    	
    	if ( num == 1) {
    		hashCode = hashCode<<16 | hashCode>>>16;   // hash function for table 2
    		hashCode = hashCode % tableSize;
    		
    		if (hashCode < 0) {
        		hashCode += tableSize;
        	}
    		return hashCode;
    	}
    	
    	return -1;
    	
    }
    
    /*
     * @param object key 
     * @return object value for the key, null if key is not in table
     */

   
    @Override
    public V get(Object key) {
    	
    	if (table1[getHash(key,0)].getKey().equals(key)) {
    		return table1[getHash(key,0)].getValue();
    	}
    	
    	if (table2[getHash(key,1)].getKey().equals(key)) {
    		return table2[getHash(key,1)].getValue();
    	}
    	
    	for ( Entry<K,V> item : overflow ) {
    		if (item.getKey().equals(key)) {
    			return item.getValue();
    		}
    	}
    	
    	return null;
    	
    }
    
    
    
    
    // helper method for put() method
    // used to get one of the two tables
    /*
     * @param int tab to indicate which table ( 0 for table1 & 1 for table2
     * @return table1 or table2
     * @return null if input is not either 0 or 1
     */
    
    private Entry<K,V>[] getTable(int tab){
    	
    	if (tab == 0) {
    		return table1;
    	}
    	
    	if (tab == 1) {
    		return table2;
    	}
    	
    	return null ;
    }
    
    /*
     * @param object key 
     * @param object value
     * @return previous value for the key if it's already in table & updates the new value in table
     * 		   null otherwise & inserts the new (k,v) object in table
     */
  
    @Override
    public V put(K key, V value) {
    	
    	// table1 -> num=0
    	int index1 = getHash(key,0);
    	// table2 -> num=1
    	int index2 = getHash(key,1);
    	
    	//if key already in table, then just update the value 
    	if ( (table1[index1] != null) && (table1[index1].getKey().equals(key))) {   // check null to prevent dot operator on null object
    		V temp = table1[index1].setValue(value);
    		return temp;
    		
    	}
    	if ( (table2[index2] != null) && (table2[index2].getKey().equals(key))) {
    		V temp = table2[index2].setValue(value);
    		return temp;
    		
    	}
    	
    	// key not already in table
    	
    	int i = 0;
    	int t = 0;
    	
    	 List<Integer> tb1 = new ArrayList<>();
    	 List<Integer> tb2 = new ArrayList<>();
    	
    	 
    	//t < START_CAPACITY // condition for detecting a cycle
 		
    	
    	 
    	 while (  true ) {  
    		// if empty spot is found, just insert the (k,v)
    		if ( getTable(i)[getHash(key,i)] == null) {   
    			getTable(i)[getHash(key,i)] = new Entry<>(key,value);
    			numKeys++;
    			return null;
    		}
    		
    		Entry<K, V> temp = getTable(i)[getHash(key,i)];  //evicting
    		
    		if (i == 0) {
    			tb1.add(getHash(key,i));              // storing which index of which table has already been bumped
    		}
    		if(i == 1) {
    			tb2.add(getHash(key,i));
    		}
    		
    		getTable(i)[getHash(key,i)] = new Entry<>(key,value); // bump
    		key = temp.getKey();      // new (k,v)
    		value = temp.getValue();
    		//t++;
    		i = (i+1)%2;
    		
    		if ( ( i == 0 && tb1.contains(getHash(key,i))) || (i == 1 && tb2.contains(getHash(key,i)))){   
    			break;       // checking whether the position we are trying to bump has already been bumped previously
    		}
    		
    	}
    	
    	// Assert : could not insert in either table
    	overflow.add( new Entry<>(key,value));
    	numKeys++;
    	return null;
    	
    }
    
    
    
    /*
     * @param object the key to be deleted
     * @return the value of the key & removes the (k,v) from  hash table
     * 		   null if the key is noth in table 
     */

    @Override
    public V remove(Object key) {
    	
    	int index1 = getHash(key,0);
    	
    	// if key in table1, set the index to null
    	if ( (table1[index1] != null) && (table1[index1].getKey().equals(key))) {
    		V oldVal = table1[index1].getValue();
    		table1[index1] = null;
    		numKeys--;
    		
    		// if any item in the overflow list has the same index, take it out of overflow and put into table1 (to reduce search time)
    		for ( Entry<K,V> item : overflow ) {
        		if (getHash(item.getKey(),0) == index1) {
        			table1[index1] = item;
        			overflow.remove(item);
        			return oldVal;
        		}
        	}
    		
    		return oldVal;
    		
    	}
    	
    	// repeat the process for table2
    	int index2 = getHash(key,1);
    	
    	if ( (table2[index2] != null) && (table2[index2].getKey().equals(key)) ) {
    		V oldVal = table2[index2].getValue();
    		table2[index2] = null;
    		numKeys--;
    		
    		for ( Entry<K,V> item : overflow ) {
        		if (getHash(item.getKey(),1) == index2) {
        			table2[index2] = item ;
        			overflow.remove(item);
        			return oldVal;
        		}
        	}
    		
    		return oldVal;
    		
    	}
    	
    	
    	// if the item is found in overflow, just remove it
    	for ( Entry<K,V> item : overflow ) {
    		if (item.getKey().equals(key)) {
    			V oldVal = item.getValue();
    			overflow.remove(item);
    			numKeys--;
    			return oldVal;
    		}
    	}
    	
    	
    	//Assert: the key is not in the table
    	return null;
    	
    	
    }
    
    
    /*
     * @return the string representing the hashTable
     */

    @Override
    public String toString() {
    	
    	String res = "";
    	
    	for (int i=0; i < table1.length ; i++) {
    		if (table1[i] != null) {
    			String add =  "[" + i + "," + table1[i].getKey().toString() + "," + table1[i].getValue().toString() + "," + "table1" + "]" + "\n" ;
    			res += add;
    		}
    	}
    	
    	for (int i=0; i < table2.length ; i++) {
    		if (table2[i] != null) {
    			String add =  "[" + i + "," + table2[i].getKey().toString() + "," + table2[i].getValue().toString() + "," + "table2" + "]" + "\n" ;
    			res += add;
    		}
    	}
    	
    	for (int i =0; i < overflow.size(); i++) {
    		String add =  "[" + i + "," + overflow.get(i).getKey().toString() + "," + overflow.get(i).getValue().toString() + "," + "overflow" + "]" + "\n";
    		res += add;
    	}
    	
    	return res;
	
    }
    
    
    //main method for the sole purpose of testing & debugging
    /*public static void main (String[] args) {
    	
    	HashtableCuckoo<Integer,String> test = new HashtableCuckoo<>();
    	
    	// test toString()
    	test.put(7, "a");
    	test.put(5, "b");
    	test.put(1,"d");
    	test.put(6, "p");
    	test.put(3, "e");
    	test.put(4, "n");
    	test.put(6, "r");
    	test.put(2, "q");
    	test.put(9, "s");
    	test.put(9, "s");
    	test.put(11, "c");
    	
    	System.out.println(test);
    	test.remove(2);
    	String  var = "csu";
    	System.out.println("------------------------------------------------------------------------");
    	System.out.println(test);
    	System.out.println(var.hashCode());
    	
    	
    	test.put(2, "a");
    	test.put(62, "b");
    	test.put(122, "c");
    	test.put(43, "f");
    	test.put(103, "g");
    	System.out.println(test);
    	System.out.println("------------------------------------------------------------------------------");
    	test.remove(122);
    	System.out.println(test);
    	System.out.println("------------------------------------------------------------------------------");
    	test.put(182,"z");
    	System.out.println(test);
    	System.out.println(test.size());
    	System.out.println(test.isEmpty()); 
    	
    	 HashtableCuckoo<String,Character> ht = new HashtableCuckoo<String, Character>();
         String[] arr1 = {"a" , "p", "w", "t", "s"};
         String[] arr2 = {"d", "n", "z", "k", "r"};
         String[] arr3 = {"b", "c", "e", "g", "h"};

         for(int i=0; i<5; i++)
         {
             String str = arr1[i] + arr2[i] + arr3[i];
             ht.put(str,'a');
             String str1 = arr2[i] + arr3[i] + arr1[i];
             ht.put(str1,'b');
             String str2 = arr3[i] + arr2[i] + arr1[i];
             ht.put(str2,'c');
         }
         ht.remove("kgt");
         ht.remove("hrs");
         StringBuilder result = new StringBuilder();
         result.append("[12,zew,b,table1]\n");
         result.append("[21,cnp,c,table1]\n");
         result.append("[33,srh,a,table1]\n");
         result.append("[42,ezw,c,table1]\n");
         result.append("[51,ncp,b,table1]\n");
         result.append("[55,adb,a,table1]\n");
         result.append("[56,gkt,c,table1]\n");
         result.append("[5,pnc,a,table2]\n");
         result.append("[9,dba,b,table2]\n");
         result.append("[17,rhs,b,table2]\n");
         result.append("[25,bda,c,table2]\n");
         result.append("[41,wze,a,table2]\n");
         result.append("[0,tkg,a,overflow]\n");
         
         System.out.println(result.toString());
         System.out.println(ht);
         System.out.println(ht.size());
         
         System.out.println("------------------------------------------------------------------------");
         
         HashtableCuckoo<String,Character> ht2 = new HashtableCuckoo<String, Character>();
         String[] arr4 = {"a" , "p", "w", "t", "s"};
         String[] arr5 = {"d", "n", "z", "k", "r"};
         String[] arr6 = {"b", "c", "e", "g", "h"};

         for(int i=0; i<5; i++)
         {
             String str = arr4[i] + arr5[i] + arr6[i];
             ht2.put(str,'a');

             String str1 = arr5[i] + arr6[i] + arr4[i];
             ht2.put(str1,'b');

             String str2 = arr6[i] + arr5[i] + arr4[i];
             ht2.put(str2,'c');

         }

         StringBuilder result2 = new StringBuilder();
         result2.append("[12,zew,b,table1]\n");
         result2.append("[21,cnp,c,table1]\n");
         result2.append("[33,hrs,c,table1]\n");
         result2.append("[42,ezw,c,table1]\n");
         result2.append("[51,ncp,b,table1]\n");
         result2.append("[55,adb,a,table1]\n");
         result2.append("[56,gkt,c,table1]\n");
         result2.append("[5,pnc,a,table2]\n");
         result2.append("[9,dba,b,table2]\n");
         result2.append("[17,rhs,b,table2]\n");
         result2.append("[25,kgt,b,table2]\n");
         result2.append("[41,wze,a,table2]\n");
         result2.append("[0,bda,c,overflow]\n");
         result2.append("[1,tkg,a,overflow]\n");
         result2.append("[2,srh,a,overflow]\n");
         
         System.out.println(result2.toString());
         System.out.println("MyCode\n");
         System.out.println(ht2);
         System.out.println(ht2.size());
    	
    }
    
    */
   

}
