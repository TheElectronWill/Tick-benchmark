package com.electronwill.tests.tasks;

/**
 *
 * @author TheElectronWill
 */
public class ClockTask implements Runnable {

	long lastTime;

	@Override
	public void run() {
		long time = System.currentTimeMillis();
		long diff = time - lastTime;
		System.out.println("time : " + time + " (+" + diff + " ms)");
		lastTime = time;
	}

}
