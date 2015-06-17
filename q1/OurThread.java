import java.util.concurrent.*;

public class OurThread extends Thread {

	Counter c;
	int numInc;
    int myId;
         
	//Constructor
	OurThread(Counter c, int numInc, int i){
		this.c = c;
		this.numInc = numInc;
        myId = i;
	}
    
	public void run() {
        for(int i=0; i<numInc; i++)	
            c.increment();
	}
    
    public int get_our_id() {
        return myId;
    }
}	
