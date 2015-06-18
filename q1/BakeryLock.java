import java.util.concurrent.atomic.*;

public class BakeryLock implements MyLock {
    private volatile int N;
	private AtomicIntegerArray number,choosing;
    
    public BakeryLock(int numProc) {
        N = numProc;
        choosing = new AtomicIntegerArray(N);
        number = new AtomicIntegerArray(N);
        for (int j = 0; j < N; j++) {
            choosing.set(j,0);
            number.set(j,0);
        }
    }
    
    @Override
    public void lock(int myId) {
        // step 1: doorway: choose a number
        choosing.set(myId,1); // choosing[myId] = true;
		int max = 0;
        for (int j = 0; j < N; j++){
            if (number.get(j) > max){
                max = number.get(j);
			}
		}
        number.set(myId, max + 1);
		choosing.set(myId,0); // choosing[myId] = false;

        // step 2: check if my number is the smallest
        for (int j = 0; j < N; j++) {
            while (choosing.get(j) == 1) ; // process j in doorway
            while ((number.get(j) != 0) &&
                   ((number.get(j) < number.get(myId)) || 
                    ((number.get(j) == number.get(myId)) && j < myId)
                   )
                  )
                ; // busy wait
        }
    }
    
    @Override
    public void unlock(int i) { // exit protocol
        number.set(i,0);
    }
}
