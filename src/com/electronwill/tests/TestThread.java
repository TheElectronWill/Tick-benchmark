package com.electronwill.tests;

import com.electronwill.tests.tasks.ClockTask;
import com.electronwill.tests.tasks.TMPgcdTask;
import com.electronwill.tests.tasks.TickMeasuringTask;
import com.electronwill.tests.tasks.TickMeasuringThread;
import com.electronwill.tests.thread.ConcurrentUpdater;

/**
 *
 * @author TheElectronWill
 */
public class TestThread {

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) throws InterruptedException {
		benchmark();
	}

	static void benchmark() {
		final int nTasks = 2000;
		TickMeasuringThread tmt = new TickMeasuringThread(nTasks);
		TickMeasuringTask.staticTmt = tmt;
		tmt.start();
		ConcurrentUpdater updater = new ConcurrentUpdater(4);
		for (int i = 0; i < nTasks; i++) {
			updater.submit(TMPgcdTask.medium());
		}
		updater.start();
	}

	static void debug() {
		ConcurrentUpdater updater = new ConcurrentUpdater(4);
		updater.submit(new ClockTask());
		updater.start();
	}
}
