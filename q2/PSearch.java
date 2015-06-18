import java.util.concurrent.*;
import java.util.ArrayList;
import java.util.List;

public class PSearch implements Callable<Integer> {
  // TODO: Declare variables 
    private int x;
	private int[] a;
	private int begin, end;	

    public PSearch(int x, int[] A, int begin, int end) {   
    // TODO: The constructor for PSearch 
    // x: the target that you want to search
    // A: the array that you want to search for the target
    // begin: the beginning index (inclusive)
    // end: the ending index (exclusive)
	    this.x = x;
	    this.a = A;
	    this.begin = begin;
	    this.end = end;
    }

    public Integer call() throws Exception {
    // TODO: your algorithm needs to use this method to get results
    // You should search for x in A within begin and end
    // Return -1 if no such target
	    for(int i=begin; i<end; i++) {
		    if(x == a[i])
			    return i;
	    }
    	return Integer.valueOf(-1);
    }

    public static int parallelSearch(int x, int[] A, int n) {
    // TODO: your search algorithm goes here
    // You should create a thread pool with n threads 
    // Then you create PSearch objects and submit those objects to the thread
    // pool

	    //Partition
	    int numP; //Number of partitions
	    int size; //Size of each partitions
	    int last; //Size of last partition (array may not split evenly)	
        
	    if(A.length < n) {
		    numP = A.length;
		    size = 1;
		    last = 1;
	    } 
        else {
		    numP = n;
		    size = (A.length/n); 
		    last = size + (A.length%n);            
	    }

	    //Thread Pool Submission
	    PSearch[] p = new PSearch[numP];
	    List<Future<Integer>> f = new ArrayList<Future<Integer>>();
	    ExecutorService tp = Executors.newFixedThreadPool(n);
         
	    for(int i=0; i<numP-1; i++) {		
		    p[i] = new PSearch(x, A, i*size, i*size+size);
		    f.add( tp.submit(p[i]) );	
	    }
        
	    p[numP-1] = new PSearch(x, A, (numP-1)*size, (numP-1)*size+last);
	    f.add( tp.submit(p[numP-1]) );	

	    //Get and aggregate results
	    try {
		    for(int i=0; i<numP; i++) {
			    int result = (f.get(i)).get();
			    if(result != -1) {
				    tp.shutdown();
				    return result;
			    }
		    }
	    } 
        catch (InterruptedException e) {
        	e.printStackTrace();
	    } 
        catch (ExecutionException e) {
        	e.printStackTrace();
	    } 
        
        tp.shutdown();
        return -1; // return -1 if the target is not found
    }
    
	public static void main(String[] args) {
    
		int[] A = {4,6,2,3,7,10,11,5,20,21};
		int result = parallelSearch(20,A,3);
        
		System.out.println("Index of search: " + result);
	}

}
