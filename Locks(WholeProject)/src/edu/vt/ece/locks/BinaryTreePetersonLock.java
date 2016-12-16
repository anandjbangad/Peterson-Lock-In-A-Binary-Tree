package edu.vt.ece.locks;

import edu.vt.ece.locks.Peterson;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

class Node {
    
    int data;
    Node left, right;
     
    Node(int d) {
        data = d;
        left = right = null;
    }
}
class BinaryTree {
    
    static Node root;
    public int thread_count_for_tree;

    Node sortedArrayToBST(int arr[], int start_point, int end_point) {
 
        
        if (start_point > end_point) {
            return null;
        }
 
        int mid = (start_point + end_point) / 2;
        Node node = new Node(arr[mid]);
 
        node.left = sortedArrayToBST(arr, start_point, mid - 1);
        node.right = sortedArrayToBST(arr, mid + 1, end_point);
         
        return node;
    }
    int height(Node root)
    {
        if (root == null)
           return 0;
        else
        {
            int lheight = height(root.left);
            int rheight = height(root.right);
            if (lheight > rheight)
                return(lheight+1);
            else return(rheight+1); 
        }
    }
    void printGivenLevel (Node root ,int level)
    {
        if (root == null)
            return;
        if (level == 1)
            System.out.print(root.data + " ");
        else if (level > 1)
        {
            printGivenLevel(root.left, level-1);
            printGivenLevel(root.right, level-1);
        }
    }
    void printLevelOrder()
    {
        
    	int h = height(root);
        int i;
        for (i=1; i<=h; i++)
            printGivenLevel(root, i);
    }
    public static void node_creations() {
    	//ArrayList<Integer> tree_values = new ArrayList<Integer>();
    	int t = BinaryTreePetersonLock.number_of_threads;
    	int arr[] = new int[t+1];
    	if(BinaryTreePetersonLock.number_of_threads!=0){
    		for(int i=1;i<=BinaryTreePetersonLock.number_of_threads;i++){
    			arr[i]=i;
    		}
    	}
        BinaryTree tree = new BinaryTree();
        root = tree.sortedArrayToBST(arr, 0, arr.length - 1);
        System.out.println("Level order traversal of binary tree is ");
        tree.printLevelOrder();
        
    }
}

public class BinaryTreePetersonLock implements Lock{
	
	public int Height_Of_Tree;
	public static int number_of_threads;
	public Peterson[][] treelevel;  
	
	final private ThreadLocal<Integer> THREAD_ID = new ThreadLocal<Integer>(){
        final private AtomicInteger id = new AtomicInteger(0); 
        protected Integer initialValue(){
            return id.getAndIncrement();
        }
    };
    
    
	public BinaryTreePetersonLock(){
		BinaryTree initialization = new BinaryTree();
		BinaryTree.node_creations();
		this.Height_Of_Tree =  (int) Math.ceil(((Math.log(number_of_threads))/Math.log(2))) ;
		System.out.println("Nume of number_of_threads :" + number_of_threads);
		System.out.println("Hence Levels In The Tree :" + Height_Of_Tree);
		this.treelevel = new Peterson[50][number_of_threads + 5];
		for(int i = 0; i < this.Height_Of_Tree; i++){
			number_of_threads = number_of_threads/2;
			for (int j = 0; j < number_of_threads; j++){
				treelevel[i][j] = new Peterson();
			}
		}
	}

	@Override
	public void lock(){
		int i = THREAD_ID.get();
		int calling_thread = THREAD_ID.get();
		int start = Height_Of_Tree;
		int mn;
		
		for(int k=0; k < Height_Of_Tree; k++)
		{
			i = (int) Math.floor(calling_thread/(Math.pow(2, (k+1))));
			mn = (int) Math.floor(calling_thread/(Math.pow(4, (k+1))));
			mn = mn+1;
			this.treelevel[k][i].lock(calling_thread);
			start --;
		}
	}

	//For unlocking your path
	@Override
	public void unlock()
	{
		int i = THREAD_ID.get();
		int j;
		int start =0;
		int calling_thread = THREAD_ID.get();
		for (int k = (Height_Of_Tree - 1) ; k>=0 ; k--)
		{
			start =k;
			i = (int) Math.floor(calling_thread/(Math.pow(2, (k + 1))));
			j = (int) Math.floor(calling_thread/(Math.pow(4, (k + 1))));
			j = j+1;
			this.treelevel[k][i].unlock(calling_thread);
		}
	}
}

