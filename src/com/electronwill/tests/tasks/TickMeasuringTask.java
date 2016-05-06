package com.electronwill.tests.tasks;

/**
 * A task that measures tick duration.
 *
 * @author TheElectronWill
 */
public class TickMeasuringTask implements Runnable {

	public static TickMeasuringThread staticTmt;
	protected final TickMeasuringThread tmt;

	public TickMeasuringTask(TickMeasuringThread tmt) {
		this.tmt = tmt;
	}

	public TickMeasuringTask() {
		this.tmt = staticTmt;
	}

	@Override
	public void run() {
		if (tmt.tickFinished) {
			synchronized (tmt) {
				if (tmt.tickFinished) {
					tmt.tickFinished = false;
					tmt.t0 = System.nanoTime();
				}
			}
		}
		tmt.latch.countDown();
	}

}
