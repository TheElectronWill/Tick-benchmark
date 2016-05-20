package com.electronwill.tests.tasks;

import static com.electronwill.tests.tasks.PgcdTask.pgcd;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author TheElectronWill
 */
public class TMHybridTask2 {

	public static class Concurrent extends TickMeasuringTask {

		final AtomicInteger counter = new AtomicInteger();

		public Concurrent(TickMeasuringThread tmt) {
			super(tmt);
		}

		@Override
		public void run() {
			//more incrementations than hybrid1
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 25; j++) {
					pgcd(i, j);
					counter.incrementAndGet();
				}
			}
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
			//more incrementations than hybrid1
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 25; j++) {
					pgcd(i, j);
					counter++;
				}
			}
			super.run();//signals task completion
		}
	}

}
