package com.electronwill.tests.tasks;

import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author TheElectronWill
 */
public class TMCounterTask extends TickMeasuringTask {

	private AtomicInteger counter = new AtomicInteger();

	public TMCounterTask(TickMeasuringThread tmt) {
		super(tmt);
	}

	@Override
	public void run() {
		counter.incrementAndGet();
		super.run();//signals task completion
	}

}
