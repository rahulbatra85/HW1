import java.util.concurrent.locks.ReentrantLock;

// TODO
// Use ReentrantLock to protect the count
public class ReentrantCounter extends Counter {
    private final ReentrantLock lock = new ReentrantLock();
    
    @Override
    public void increment() {
        lock.lock();  // block until condition holds
        try {
          // ... method body
        	count++;
        } 
        finally {
          lock.unlock();
        }
    }
}
