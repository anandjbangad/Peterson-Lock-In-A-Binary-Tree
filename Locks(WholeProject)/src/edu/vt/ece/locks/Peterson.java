package edu.vt.ece.locks;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import edu.vt.ece.bench.ThreadId;

public class Peterson implements Lock{
	public static int THREAD_COUNT;
	private AtomicBoolean flag[] = new AtomicBoolean[THREAD_COUNT];
	private AtomicInteger victim;
	public Peterson() {
		for (int i = 0; i < THREAD_COUNT; i++){
			flag[i] = new AtomicBoolean();
			flag[i].set(false);
		}
		victim = new AtomicInteger();
	}
	
	@Override
	public void lock() {
		int i = ((ThreadId)Thread.currentThread()).getThreadId();
		System.out.println("Thread"+ " "+ i+ " "+ "acquiring lock");
		int j = 1-i;
		flag[i].set(true);
		victim.set(i);
		while(flag[j].get() && victim.get() == i);
//			System.out.println("Thread " + i + " waiting");
	}

	@Override
	public void unlock() {
		int i = ((ThreadId)Thread.currentThread()).getThreadId();
		flag[i].set(false);
	}
	
	
	public void lock(int i) {
		boolean one_two;
		i = ((ThreadId)Thread.currentThread()).getThreadId();
		
        one_two = true;
        flag[i].set(true);
        victim.set(i);
        while (one_two && victim.get() == i){
        	int check_other_thread = 0;
        	for (int k = 0; k < THREAD_COUNT ;k++){
	        	if (k != i && flag[k].get()){
	        		one_two = true;
	        		check_other_thread++;
	        	}	
	        }
        	if (check_other_thread == 0){
        		one_two = false;
        	}
        }

    }
	public void unlock(int i){
		i = ((ThreadId)Thread.currentThread()).getThreadId();
        flag[i].set(false);  

    }

}
