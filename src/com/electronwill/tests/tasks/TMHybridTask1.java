package com.electronwill.tests.tasks;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author TheElectronWill
 */
public class TMHybridTask1 {

	public static class Concurrent extends TickMeasuringTask {

		final AtomicInteger counter = new AtomicInteger();
		final PgcdTask task = PgcdTask.small();

		public Concurrent(TickMeasuringThread tmt) {
			super(tmt);
		}

		@Override
		public void run() {
			//more computations than incrementations
			counter.incrementAndGet();
			task.run();
			counter.decrementAndGet();
			super.run();//signals task completion
		}
	}

	public static class Simple extends TickMeasuringTask {

		int counter = 0;
		final PgcdTask task = PgcdTask.small();

		public Simple(TickMeasuringThread tmt) {
			super(tmt);
		}

		@Override
		public void run() {
			//more computations than incrementations
			counter++;
			task.run();
			counter--;
			super.run();//signals task completion
		}
	}

}
