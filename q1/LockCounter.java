// TODO
// Use MyLock to protect the count
// This counter can use either BakeryLock or FastMutexLock to protect the count

public class LockCounter extends Counter {

    private final MyLock lock;
    
    public LockCounter(MyLock lock) {
        this.lock = lock;
    }

    @Override
    public void increment() {  
        //lock using LockCounter
        OurThread t = (OurThread) Thread.currentThread();
        int id = t.get_our_id();  
        
        lock.lock(id);
        
        try {
            // ... method body
            count++;
        } 
        finally {
            //lock LockCounter
            lock.unlock(id);
        }        
    }
}
