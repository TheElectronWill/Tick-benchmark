package com.electronwill.tests;

import com.electronwill.tests.tasks.ClockTask;
import com.electronwill.tests.tasks.TMPgcdTask;
import com.electronwill.tests.tasks.TickMeasuringTask;
import com.electronwill.tests.tasks.TickMeasuringThread;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author TheElectronWill
 */
public class TestExecutor {

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
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(4);
		for (int i = 0; i < nTasks; i++) {
			executor.scheduleAtFixedRate(TMPgcdTask.medium(), 0, 50, TimeUnit.MILLISECONDS);
		}
	}

	static void debug() {
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(4);
		executor.scheduleAtFixedRate(new ClockTask(), 0, 50, TimeUnit.MILLISECONDS);
	}

}
