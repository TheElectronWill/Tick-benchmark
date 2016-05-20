package com.electronwill.tests.tasks;

import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author TheElectronWill
 */
public class TMCounterTask {

	public static class Concurrent extends TickMeasuringTask {

		final AtomicInteger counter = new AtomicInteger();

		public Concurrent(TickMeasuringThread tmt) {
			super(tmt);
		}

		@Override
		public void run() {
			counter.incrementAndGet();
			super.run();//signals task completion
		}
	}

	public static class Simple extends TickMeasuringTask {

		int counter = 0;

		public Simple(TickMeasuringThread tmt) {
			super(tmt);
		}

		@Override
		public void run() {
			counter++;
			super.run();//signals task completion
		}
	}

}
