// TODO
// Implement the bakery algorithm
// Remember to use volatile qualifier for shared variables to guarantee atomicity
import java.util.concurrent.atomic.*;

public class BakeryLock2 implements MyLock {
    
    private volatile int numThread;
    private AtomicBoolean[] choosing; 
    private AtomicIntegerArray queue_num; //private volatile int[] queue_num;

    //init lock
    public BakeryLock2(int numThread) {
    
        this.numThread = numThread;
        
        choosing = new AtomicBoolean[numThread]; 
        queue_num = new AtomicIntegerArray(numThread); // initialized to 0 by default        
        
        //init choosing[] to all false and queue_num[] to all 0s
        for (int j=0; j<numThread; j++) {
            choosing[j] = new AtomicBoolean(); // initialized to false by default
            //queue_num[j] = 0;
        }
    }

    @Override
    public void lock(int myId) {
    
        //doorway -- grab queue_num (+1 from highest)
        choosing[myId].set(true);        
        for (int j=0; j<numThread; j++) {
            if ( queue_num.get(j) > queue_num.get(myId) )  
                queue_num.set(myId, queue_num.get(j));            
        }               
        queue_num.incrementAndGet(myId); //queue_num[myId]++;    <<<< THE MAIN SOURCE OF THE FKUP
        choosing[myId].set(false); 

    
        //in queue -- wait for queue_num to be next (smallest from others)
        for (int j=0; j<numThread; j++) {
            while (choosing[j].get()); // wait for thread j to get queue_num
            while ( (queue_num.get(j)  != 0) && //thread j is done
                     ( (queue_num.get(j) < queue_num.get(myId)) || //thread j has a smaller queue_num
                       (queue_num.get(j) == queue_num.get(myId)) && (j < myId)  //check pids
                     )
                  ); //wait for other threads to finish    
        }  
    }

    @Override
    public void unlock(int myId) {
        queue_num.set(myId, 0); //done
    }
}
