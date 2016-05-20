package com.electronwill.tests.thread;

import com.electronwill.tests.Stoppable;
import java.util.concurrent.locks.LockSupport;

/**
 * @author TheElectronWill
 */
public class ConcurrentUpdater implements Stoppable {

	private final Worker[] threads;
	private int i = 0;

	public ConcurrentUpdater(int nThreads) {
		this.threads = new Worker[nThreads];
		for (int j = 0; j < nThreads; j++) {
			threads[j] = new Worker(j, new SynchronizedBag<>(5000));

		}
	}

	public void start() {
		for (Worker thread : threads) {
			thread.start();
		}
	}

	public void stopNicely() {
		for (Worker thread : threads) {
			thread.stopNicely();
		}
	}

	public void stopNow() {
		for (Worker thread : threads) {
			thread.stopNow();
		}
	}

	public synchronized void submit(Runnable r) {
		threads[i++].toUpdate.add(r);
		if (i == threads.length) {
			i = 0;
		}
	}

	public void awaitTermination() throws InterruptedException {
		for (Worker thread : threads) {
			thread.t.join();
		}
	}

	public class Worker extends NiceThread {

		final Bag<Runnable> toUpdate;
		volatile long tickDuration = 0;// in nanoseconds
		final int id;

		Worker(int id) {
			this(id, new SynchronizedBag<>(200, 10));
		}

		Worker(int id, Bag<Runnable> updateables) {
			this.toUpdate = updateables;
			this.id = id;
		}

		@Override
		public void doRun() {
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

}
