// TODO
// Use MyLock to protect the count
// This counter can use either BakeryLock or FastMutexLock to protect the count

public class LockCounter extends Counter {
    public LockCounter(MyLock lock) {
    }

    @Override
    public void increment() {
    }
}
