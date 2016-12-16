package edu.vt.ece.locks;

import java.util.concurrent.atomic.AtomicInteger;

import edu.vt.ece.bench.ThreadId;

public class LExclusion implements Lock { 
	
	private AtomicInteger[] level;
	private AtomicInteger reached_CS;
	private AtomicInteger count_down;
	private AtomicInteger[] victim;
	public static int THREAD_ALLOWED;
	public static int THREAD_COUNT;
	public LExclusion() {
		this(THREAD_COUNT,THREAD_ALLOWED);
		
	}
	public LExclusion(int n, int l){
		reached_CS = new AtomicInteger();
		level = new AtomicInteger[n+1]; 
		victim = new AtomicInteger[n-l];
		count_down = new AtomicInteger();
		for (int i = 0; i < n; i++) { 
			level[i] = new AtomicInteger();
			level[i].set(0);
		} 
		for (int i = 0; i < n-l; i++) { 
			victim[i] = new AtomicInteger();
			victim[i].set(0);
		} 
		count_down.set(0);
		reached_CS.set(0);
		
	}
	@Override
	public void lock() {
		int me = ((ThreadId)Thread.currentThread()).getThreadId(); 
		for (int i = 1; i < THREAD_COUNT-THREAD_ALLOWED; i++) { 
			victim[i].set(me); 	
			level[me].set(i);
			while (reached_CS.get() >= THREAD_ALLOWED && victim[i].get() == me);
			
		}
		reached_CS.set(reached_CS.get()+1);
	}
	
	@Override
	public void unlock() { 
		int me = ((ThreadId)Thread.currentThread()).getThreadId();
		level[me].set(0);
		reached_CS.set(reached_CS.get()-1);
	}
}
