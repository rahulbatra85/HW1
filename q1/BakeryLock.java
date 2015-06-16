// TODO
// Implement the bakery algorithm
// Remember to use volatile qualifier for shared variables to guarantee atomicity

public class BakeryLock implements MyLock {
    
    volatile int numThread;
    volatile boolean[] choosing;
    volatile int[] queue_num;

    //init lock
    public BakeryLock(int numThread) {
    
        this.numThread = numThread;
        choosing = new boolean[numThread];
        queue_num = new int[numThread];
        
        //init choosing[] to all false and queue_num[] to all 0s
        for (int j=0; j<numThread; j++) {
            choosing[j] = false;
            queue_num[j] = 0;
        }
    }

    @Override
    public void lock(int myId) {
    
        //doorway -- grab queue_num (+1 from highest)
        choosing[myId] = true;
        
        for (int j=0; j<numThread; j++) {
            if (queue_num[j] > queue_num[myId]) 
                queue_num[myId] = queue_num[j];            
        }  
              
        queue_num[myId]++;     
        choosing[myId] = false;

    
        //in queue -- wait for queue_num to be next (smallest from others)
        for (int j=0; j<numThread; j++) {
            while (choosing[j]); // wait for thread j to get queue_num
            while ( (queue_num[j]  != 0) && //thread j is done
                     ( (queue_num[j] < queue_num[myId]) || //thread j has a smaller queue_num
                        (queue_num[j] == queue_num[myId]) && (j<myId) //thread j has same queue_num (wtf) but got here 1st
                      )
                   ); //wait for other threads to finish    
        }  
    }

    @Override
    public void unlock(int myId) {
        queue_num[myId] = 0; //done
    }
}
