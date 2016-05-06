package com.electronwill.tests.tasks;

import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author TheElectronWill
 */
public class CounterTask implements Runnable {

	private AtomicInteger counter = new AtomicInteger();

	@Override
	public void run() {
		counter.incrementAndGet();
	}

}
