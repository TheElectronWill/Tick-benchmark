package com.electronwill.tests.tasks;

import com.electronwill.tests.thread.NiceThread;
import java.util.concurrent.CountDownLatch;

/**
 *
 * @author TheElectronWill
 */
public class TickMeasuringThread extends NiceThread {

	CountDownLatch latch;
	final int nTasks;
	volatile boolean tickFinished = true;
	volatile long t0;

	public TickMeasuringThread(int nTasks) {
		super("TickMeasuringThread");
		this.nTasks = nTasks;
		this.latch = new CountDownLatch(nTasks);
	}

	@Override
	public void doRun() throws InterruptedException {
		while (run) {
			latch.await();
			long t1 = System.nanoTime();

			tickFinished = true;
			latch = new CountDownLatch(nTasks);

			double ms = (t1 - t0) / 1000_000.0;
			System.out.println("tick : " + ms + " millisecondes");
		}
	}

}
