package com.electronwill.tests.tasks;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author TheElectronWill
 */
public class TMHashMapTask extends TickMeasuringTask implements Runnable {

	final Map<Integer, String> map;

	public TMHashMapTask(TickMeasuringThread tmt, boolean concurrent) {
		super(tmt);
		if (concurrent) {
			map = new ConcurrentHashMap<>();
		} else {
			map = new HashMap<>();
		}
		init();
	}

	public TMHashMapTask(TickMeasuringThread tmt, Map<Integer, String> map) {
		super(tmt);
		this.map = map;
		init();
	}

	private void init() {
		map.put(0, "abc");
	}

	@Override
	public void run() {
		int i = map.get(0).charAt(0);
		map.put(i, "azertyuiop");
		super.run();
	}

}
