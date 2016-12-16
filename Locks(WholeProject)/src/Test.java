import edu.vt.ece.bench.Counter;
import edu.vt.ece.bench.SharedCounter;
import edu.vt.ece.bench.TestThread;
import edu.vt.ece.locks.*;

/**
 * 
 * @author Mohamed M. Saad
 */
public class Test {

	private static final int THREAD_COUNT = 50;
	
	private static final String LOCK_ONE = "LockOne";
	private static final String LOCK_TWO = "LockTwo";
	private static final String PETERSON = "Peterson";
	private static final String FILTER = "Filter";

	public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		String lockClass = (args.length==0 ? PETERSON : args[0]);
		Peterson.THREAD_COUNT = THREAD_COUNT;
		Filter.THREAD_COUNT = THREAD_COUNT;
		final Counter counter = new SharedCounter(0, (Lock)Class.forName("edu.vt.ece.locks." + lockClass).newInstance());
		for(int t=0; t<THREAD_COUNT; t++){            //typecast lock
			new TestThread(counter).start();
			//System.out.println("Thread id"+ ((TestThread)Thread.currentThread()).getThreadId()+ "generated");
			}
		//System.out.println("Average time per thread is " + totalTime/THREAD_COUNT + "ms");
	}
}
