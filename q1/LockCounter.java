// TODO
// Use MyLock to protect the count
// This counter can use either BakeryLock or FastMutexLock to protect the count

public class LockCounter extends Counter {

    private volatile MyLock lock;
    
    public LockCounter(MyLock lock) {
        this.lock = lock;
    }

    @Override
    public void increment() {  
    
        OurThread t = (OurThread) Thread.currentThread();
        final int id = t.get_our_id();
          
        //lock using LockCounter        
        lock.lock(id);
        
        try {
            ++count;
        }
        finally {
            //unlock LockCounter
            lock.unlock(id);
        }
    }
}
