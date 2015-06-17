import java.util.concurrent.*;

public class OurThread extends Thread {

	private volatile Counter c;
	private int numInc;
    private int myId;
         
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
