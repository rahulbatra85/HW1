import java.util.concurrent.locks.ReentrantLock;

// TODO
// Use ReentrantLock to protect the count
public class ReentrantCounter extends Counter {
    @Override
    public void increment() {
    	ReentrantLock lock = new ReentrantLock();
        lock.lock();  // block until condition holds
        try {
          // ... method body
        	count++;
        } finally {
          lock.unlock();
        }
    }
}
