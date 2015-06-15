import java.util.concurrent.*;

public class OurThread implements Callable<Integer> {
	Counter c;
	int numInc;

	//Constructor
	OurThread(Counter c, int numInc){
		this.c = c;
		this.numInc = numInc;
	}

	public Integer call() throws Exception {
		for(int i=0; i<numInc; i++){
			c.increment();
		}
	}
}	

