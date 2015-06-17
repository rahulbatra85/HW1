import java.util.concurrent.*;

public final class OurThread extends Thread {

    private final Counter c;
	private final int numInc;
    private final int myId;  
         
	//Constructor
	public OurThread(Counter c, int numInc, int i){
		this.c = c;
		this.numInc = numInc;
        this.myId = i;
	}
    
	public void run() {
        for(int i=0; i<numInc; i++)	
            c.increment();
	}
    
    public int get_our_id() {
        return myId;
    }
}	
