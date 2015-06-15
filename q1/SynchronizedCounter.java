// TODO 
// Use synchronized to protect count
public class SynchronizedCounter extends Counter {
    @Override
    public synchronized void increment() {
		count++;
    }
}
