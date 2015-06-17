import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.*;

public class BakeryLock implements MyLock {
    volatile int N;
//    volatile boolean[] choosing; // inside doorway
//    volatile int[] number;
	AtomicIntegerArray number,choosing;
    
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
    public void lock(int tid) {
        // step 1: doorway: choose a number
//        choosing[tid] = true;
        choosing.set(tid,1);
		int max = 0;
        for (int j = 0; j < N; j++){
            if (number.get(j) > max){
                max = number.get(j);
			}
		}
        number.set(tid, max + 1);
		choosing.set(tid,0);

        // step 2: check if my number is the smallest
        for (int j = 0; j < N; j++) {
<<<<<<< HEAD
            while (choosing[j]) ; // process j in doorway
            while ( number[j] != 0  &&
                    ( number[j] < number[i]  ||
                    ( number[j] == number[i] && j < i )))
=======
            //while (choosing[j]) ; // process j in doorway
            while (choosing.get(j) == 1) ; // process j in doorway
            while ((number.get(j) != 0) &&
//                    ((number[j] < number[tid]) || ((number[j] == number[tid]) && j < tid)))
                    ((number.get(j) < number.get(tid)) || ((number.get(j) == number.get(tid)) && j < tid)))
>>>>>>> 5ecfe1e0969e50fc126785bb875fd207cd2161ca
                ; // busy wait
        }
    }
    
    @Override
    public void unlock(int i) { // exit protocol
        number.set(i,0);
    }
}
