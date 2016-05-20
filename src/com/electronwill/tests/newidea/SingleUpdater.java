package com.electronwill.tests.newidea;

import com.electronwill.tests.thread.Bag;
import com.electronwill.tests.thread.NiceThread;
import com.electronwill.tests.thread.SimpleBag;
import java.util.concurrent.locks.LockSupport;

/**
 *
 * @author TheElectronWill
 */
public class SingleUpdater extends NiceThread {

	private final Bag<Runnable> toUpdate;
	volatile long tickDuration = 0;// in nanoseconds

	public SingleUpdater(int capacity) {
		this.toUpdate = new SimpleBag<>(capacity);
	}

	public SingleUpdater(int capacity, int capacityIncrement) {
		this.toUpdate = new SimpleBag<>(capacity, capacityIncrement);
	}

	public void submit(Runnable task) {
		toUpdate.add(task);
	}

	@Override
	protected void doRun() throws Exception {
		while (run) {// Main loop. 1 tick = 50 ms = 50 000 000 ns
			long t0 = System.nanoTime();// Initial time in nanoseconds.

			// == Game update ==
			for (int i = 0; i < toUpdate.size(); i++) {
				Runnable u = toUpdate.get(i);
				try {
					u.run();
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			long t1 = System.nanoTime();
			tickDuration = t1 - t0;

			// == Wait for the next tick ==
			long timeElapsed;
			while ((timeElapsed = System.nanoTime() - t0) < 50_000_000) {
				// == Wait ==
				if (timeElapsed < 42_000_000) {// time left > 8ms
					LockSupport.parkNanos(5_000_000);// parks for 5ms
				}
			}
		}
	}

}
