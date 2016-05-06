package com.electronwill.tests.thread;

/**
 * @author TheElectronWill
 */
public class ConcurrentUpdater {

	private final UpdateThread[] threads;
	private int i = 0;

	public ConcurrentUpdater(int nThreads) {
		this.threads = new UpdateThread[nThreads];
		for (int j = 0; j < nThreads; j++) {
			threads[j] = new UpdateThread(j, new SimpleBag<>(5000));

		}
	}

	public void start() {
		for (UpdateThread thread : threads) {
			thread.start();
		}
	}

	public void stopNicely() {
		for (UpdateThread thread : threads) {
			thread.stopNicely();
		}
	}

	public void stopNow() {
		for (UpdateThread thread : threads) {
			thread.stopNow();
		}
	}

	public void submit(Runnable r) {
		threads[i++].toUpdate.add(r);
		if (i == threads.length) {
			i = 0;
		}
	}

}
