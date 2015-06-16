// TODO 
// Implement Fast Mutex Algorithm
// Remember to use volatile qualifier for shared variables to guarantee atomicity

public class FastMutexLock implements MyLock {

    volatile int X,Y;
    volatile boolean[] flag;
    volatile int numThread;


    public FastMutexLock(int numThread) {
    // TODO: initialize your algorithm      
      
        this.numThread = numThread;
        X = -1;
        Y = -1;      
    }

    @Override
    public void lock(int myId) {
      // TODO: the locking algorithm
        
        while(true) {
        
            flag[myId] = true;
            X = myId;
            
            if (Y!=-1) { //LEFT QUEUE
                flag[myId] = false;
                while (Y!=-1) ; //wait until Y:=-1
                continue; //start all over
            }
            else { 
                Y = myId;
                if (X==myId) 
                    return; // fast path, exit to CS
                    
                else { // RIGHT QUEUE
                    flag[myId] = false;
                    
                    for (int j=0; j<numThread; j++) 
                        while (flag[j]); // wait until flag[j]:=false
                     
                    if (Y==myId) 
                        return; // slow path, exit to CS
                    else {
                        while (Y!=-1) ; //wait until Y:=-1
                        continue; //start all over
                    }
                }//end right queue else
            }// end not left queue else                
        }//end while true
    }

    @Override
    public void unlock(int myId) {
      // TODO: the unlocking algorithm
      Y = -1; 
      flag[myId] = false;
      
    }
}
