package edu.vt.ece.bench;

import edu.vt.ece.locks.Lock;

/**
 * 
 * @author Mohamed M. Saad
 */
public class SharedCounter extends Counter{
	private Lock lock;

	public SharedCounter(int c, Lock lock) {
		super(c); // parent counter constructor run
		this.lock = lock;
	}
	
	@Override
	public int getAndIncrement() {
		lock.lock();
		int temp = -1;
		try {
			temp = super.getAndIncrement();
			//System.out.println(temp);
			//System.out.println("Thread id"+" " +((TestThread)Thread.currentThread()).getThreadId()+" "+ "entered Critcal Section");
			
		} finally {
			lock.unlock();
		}
		return temp;
	}

}
