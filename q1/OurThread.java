import java.util.concurrent.*;

public class OurThread implements Runnable {
	Counter c;
	int numInc;

	//Constructor
	OurThread(Counter c, int numInc){
		this.c = c;
		this.numInc = numInc;
	}

	public void run() {
		for(int i=0; i<numInc; i++){
			c.increment();
		}
	}
}	

