package hw4;

import java.util.Random;

/**
 * A class to represent a treap, that is, a BST with node placement
 * randomized by probabilistic heap-like priorities
 * @author Mohammad Khan
 */
public class Treap<E extends Comparable<E>> extends BinarySearchTree<E> {
    
	protected static class Node<E> {
		
		public E data; // key for the search
		public int priority; // random heap priority
		public Node<E> left;
		public Node<E> right;
	
		/** Creates a new node with the given data and priority. The
		 *  pointers to child nodes are null. Throw exceptions if data
		 *  is null. 
		 */
		public Node(E data, int priority) {
			if (data == null) {
				throw new IllegalArgumentException("Data can't be empty!");
			}
			
			this.data = data;
			this.priority = priority;
			left = null;
			right = null;
	
		}
		
		public Node<E> rotateRight() {
		    Node<E>original = this;
		    Node<E>modified ;
		    modified = this.left;
		    Node<E>temp = modified.right;
		    modified.right = original;
		    modified.right.left = temp;
		    return modified;
		    
		}
	
		public Node<E> rotateLeft() {
		   Node<E>modified;
		   Node<E>original = this;
		   modified = this.right;
		   Node<E>temp = modified.left;
		   modified.left = original;
		   modified.left.right = temp;
		   return modified;
		}
		
		public String toString() {
            return "(" + "key=" + data.toString() + ", " + "priority=" + Integer.toString(priority) + ")";
        }
    }

    private Random priorityGenerator;
    private Node<E> root;

    /** Create an empty treap. Initialize {@code priorityGenerator}
     * using {@code new Random()}. See {@url
     * http://docs.oracle.com/javase/8/docs/api/java/util/Random.html}
     * for more information regarding Java's pseudo-random number
     * generator. 
     */
    public Treap() {
    	priorityGenerator = new Random();
    	root = null;
    }


    /** Create an empty treap and initializes {@code
     * priorityGenerator} using {@code new Random(seed)}
     */
    public Treap(long seed) {
		priorityGenerator = new Random(seed);
    	root = null;
    }
    
    
    public  boolean add(E key, int priority) {
    	root = add(root,key,priority);
    	return addReturn;
    }
    
    public boolean add(E key) {
    	root = add(root,key,priorityGenerator.nextInt());
    	 return addReturn;
    }

    private Node<E> add(Node<E> localRoot, E key, int priority) {
    	
    	if (localRoot == null) {
            addReturn = true;
            return new Node<E>(key,priority);
        } 
    	else if (key.compareTo(localRoot.data) == 0) {
            addReturn = false;
            return localRoot;
        } 
    	else if (key.compareTo(localRoot.data) < 0) {
            
    		localRoot.left = add(localRoot.left, key, priority);
            
            if (localRoot.left.priority < localRoot.priority) {
            	localRoot = localRoot.rotateRight();
            }
            return localRoot;
        } 
    	else {
            localRoot.right = add(localRoot.right,key, priority);
            
            if (localRoot.right.priority < localRoot.priority) {
            	localRoot = localRoot.rotateLeft();
            }
            
            return localRoot;
        }
	
    }

    public E delete(E key) {
    	root = delete(root,key);
    	return deleteReturn;
    }
    
    private Node<E> delete(Node<E> localRoot, E key) {
    	
    	Node<E>parent = null; //parent of the node containing key
        if (localRoot == null) {
            // item is not in the tree.
            deleteReturn = null;
            return localRoot;
        }

        // Search for item to delete.
        int compResult = key.compareTo(localRoot.data);
        if (compResult < 0) {
            localRoot.left = delete(localRoot.left, key);
            return localRoot;
        } 
        else if (compResult > 0) {
            localRoot.right = delete(localRoot.right, key);
            return localRoot;
        } 
        else {
        	
        	if (localRoot.left == null && localRoot.right != null) {
        		localRoot = parent = localRoot.rotateLeft();
        		localRoot.left  = delete(parent.left,key);
        		return localRoot;
        		
        	}
        	if(localRoot.right == null && localRoot.left != null) {
        		localRoot = parent = localRoot.rotateRight();
        		localRoot.right = delete(parent.right,key);
        		return localRoot;
        		
        	}
        	
        	if (localRoot.right!= null && localRoot.left != null) {
        		
        		if (localRoot.right.priority > localRoot.left.priority) {
	        		localRoot = parent = localRoot.rotateRight();
	        		localRoot.right  = delete(parent.right,key);
	        		return localRoot;
	        		
	        	}
	        	else {
	        		localRoot = parent = localRoot.rotateLeft();
	        		localRoot.left = delete(parent.left,key);
	        		return localRoot;
				}
			}
        	else{
       		 deleteReturn = localRoot.data;
       		 localRoot = null;
				return localRoot;
			}
        }
    }
    

    private E find(Node<E> localRoot, E key) {
    	 
    	if (localRoot == null) {
    		return null;
         }

         // Compare the target with the data field at the root.
         int compResult = key.compareTo(localRoot.data);
         if (compResult == 0) {
             return localRoot.data;
         } else if (compResult < 0) {
             return find(localRoot.left, key);
         } else {
             return find(localRoot.right, key);
         }
	
    }

    public E find(E key) {
    	return find(root, key);
	}
    
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        preOrderTraverse(root, 1, sb);
        return sb.toString();
    }

    /**
     * Perform a preorder traversal.
     * @param node The local root
     * @param depth The depth
     * @param sb The string buffer to save the output
     */
    private void preOrderTraverse(Node<E> node, int depth,
            StringBuilder sb) {
        for (int i = 1; i < depth; i++) {
            sb.append("  ");
        }
        if (node == null) {
            sb.append("null\n");
        } else {
            sb.append(node.toString());
            sb.append("\n");
            preOrderTraverse(node.left, depth + 1, sb);
            preOrderTraverse(node.right, depth + 1, sb);
        }
    }
   
    
    //main method used for testing and debugging process 
    /*public static void main (String[] args) {
    	Treap<Integer> testTree = new Treap < Integer >();
    	testTree.add (4 ,81);
    	testTree.add (2 ,69);
    	testTree.add (6 ,30);
    	testTree.add (1 ,16);
    	testTree.add (3 ,88);
    	testTree.add (5 ,17);
    	testTree.add (7 ,74);
    	 
    	String test2 = testTree.toString().replaceAll("[\\n\\t ]", "").toLowerCase();
    	System.out.println(test2);
    	System.out.println(testTree);
    	 
    	 testTree.delete(5);
    	 
        Object val = testTree.find(50);
    	System.out.println(val);
    	
    	 
    	//System.out.println(testTree);
    	
    	testTree.delete(10);
    	//System.out.println(testTree);
    	testTree.add(15,43);
    	//System.out.println(testTree);
    	testTree.add(15,43);
    	//System.out.println(testTree);
    	
    	
    	
    	
    	testTree.delete(100);
    	testTree.delete(1);
    	testTree.add(32,504);
    	System.out.println(testTree);
    	
    	Object val2 = testTree.find(1);
    	System.out.println(val2);
    	
    	//test string 
    	
    	String test;
    	
    	test= "(key=1,priority=16)null(key=5,priority=17)(key=2,priority=69)null(key=4,priority=81)(key=3,priority=88)nullnullnull(key=6,priority=30)null(key=7,priority=74)nullnull";
    			//"(key=1,priority=16)null(key=5,priority=17)"
    	//System.out.println(test);
    	
    	System.out.println(test.equals(test2));
    			
    	
    	
    }*/
}
