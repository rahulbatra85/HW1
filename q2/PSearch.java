import java.util.concurrent.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

	public static void main(String[] args){
		int[] A = {4,6,2,3,7,10,11,5,20,21};				

		//First Element
		int search = 4;
		int numThreads = 6;
		int result = parallelSearch(search,A,numThreads);
		if(result != 0){
			System.out.println("ERROR: Test=First Element, Search="+search+ "Array="+ A.toString() +",resultIdx=" + result);
				System.exit(-1);
		}else{
			System.out.println("PASSED: Test=First Element, Search="+search+ "Array="+ A.toString() +",resultIdx=" + result);
		}
	
		//Second Element
		search = 6;
		numThreads = 6;
		result = parallelSearch(search,A,numThreads);
		if(result != 1){
			System.out.println("ERROR: Test=Second Element, Search="+search+ "Array="+ A.toString() +",resultIdx=" + result);
				System.exit(-1);
		}else{
			System.out.println("PASSED: Test=Second Element, Search="+search+ "Array="+ A.toString() +",resultIdx=" + result);
		}
	
		//Middle Element
		search = 10;
		numThreads = 3;
		result = parallelSearch(search,A,numThreads);
		if(result != 5){
			System.out.println("ERROR: Test=Middle Element, Search="+search+ "Array="+ A.toString() +",resultIdx=" + result);
				System.exit(-1);
		}else{
			System.out.println("PASSED: Test=Middle Element, Search="+search+ "Array="+ A.toString() +",resultIdx=" + result);
		}
		
	
		//Last Element
		search = 21;
                numThreads = 6;

		result = parallelSearch(search,A,numThreads);
		if(result != 9){
			System.out.println("ERROR: Test=Last Element, Search="+search+ "Array="+ A.toString() +",resultIdx=" + result);
				System.exit(-1);
		}else{
			System.out.println("PASSED: Test=Last Element, Search="+search+ "Array="+ A.toString() +",resultIdx=" + result);
		}

		Random rand = new Random();
	
		for(int i=0; i<10000; i++){	
			//Random Array Size
			int size = rand.nextInt(100000) + 1;
			int[] rA = new int[size];
			//Randomly pick array elements
			for(int n=0; n<size; n++){
				rA[n] = rand.nextInt(1000000) + 1;
			}

			//Randomly Pick element to search for or pick one that's not in the array
			int idx = rand.nextInt(size) + 1;
			idx--;
			if(rand.nextInt(10) + 1 > 1){
				search = rA[idx];
				for(int n=0; n<size; n++){
					if(rA[n] == search){
						idx = n;
						n = size;
					}
				}
			} else{
				search = rand.nextInt(1000000) + 1000000;
				idx = -1;
			}

			//Pick number of threads
			numThreads = rand.nextInt(20) + 1;
			System.out.println("INFO: Test=Random Element " + i + ", ArraySize="+size+" idx="+idx+ " search="+search+ " numThreads="+numThreads);

			result = parallelSearch(search,rA,numThreads);
			if(result != idx){
				System.out.println("ERROR: Test=Random Element, Search="+search+ " Array="+ rA.toString() +" resultIdx=" + result);
				System.exit(-1);
			}else{
				System.out.println("PASSED: Test=Random Element, Search="+search+ "Array="+ rA.toString() +" resultIdx=" + result);
			}
		}

	}

}
