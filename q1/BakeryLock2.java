// TODO
// Implement the bakery algorithm
// Remember to use volatile qualifier for shared variables to guarantee atomicity

public class BakeryLock2 implements MyLock {
    
    private static volatile int numThread;
    private volatile boolean[] choosing;
    private volatile int[] queue_num;
    
    //init lock
    public BakeryLock2(int numThread) {
    
        this.numThread = numThread;
        this.choosing = new boolean[numThread];
        this.queue_num = new int[numThread];
        
        //init choosing[] to all false and queue_num[] to all 0s
        for (int j=0; j<numThread; j++) {
            choosing[j] = false;
            queue_num[j] = 0;
        }
    }

    @Override
    public void lock(int myId) {
    
        queue_num[myId] = 0; //make sure num==0 !!
        
        //doorway -- grab queue_num (+1 from max)
        choosing[myId] = true; 
               
        //int max = 0;  
        //for (id : queue_num) { if (id > max) max = id;  }      
        //queue_num[myId] = max + 1;  
        
        for (int j = 0; j < numThread; j++)
            if (queue_num[j] > queue_num[myId])
                queue_num[myId] = queue_num[j];
        queue_num[myId]++;
           
        choosing[myId] = false;

    
        //in queue -- wait for queue_num to be next (smallest from others)
        for (int j = 0; j<numThread; j++) {
            
            while (choosing[j]) { OurThread.yield(); }// wait for thread j to get queue_num
            while ( queue_num[j] != 0 && //thread j is done
                    ( queue_num[myId] > queue_num[j] || //thread j has a smaller queue_num
                      ( queue_num[myId] == queue_num[j] && myId > j ) //thread j has same queue_num(wtf) but got here 1st
                    ) //end big ||
                  )//end big &&  
                  { OurThread.yield(); } //wait for other threads to finish    
        }//end for 
    }

    @Override
    public void unlock(int myId) {
        queue_num[myId] = 0; //done
    }
}
