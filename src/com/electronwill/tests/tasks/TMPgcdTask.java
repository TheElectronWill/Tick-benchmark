package com.electronwill.tests.tasks;

/**
 *
 * @author TheElectronWill
 */
public class TMPgcdTask extends TickMeasuringTask {

	private static int pgcd(int a, int b) {
		return b == 0 ? a : pgcd(b, a % b);
	}

	private final int i, j;

	public TMPgcdTask(int i, int j, TickMeasuringThread tmt) {
		super(tmt);
		this.i = i;
		this.j = j;
	}

	/**
	 * Mini task which takes almost 0 time to complete.
	 */
	public static TMPgcdTask mini(TickMeasuringThread tmt) {
		return new TMPgcdTask(1, 1, tmt);
	}

	/**
	 * 10 times bigger than mini.
	 */
	public static TMPgcdTask tiny(TickMeasuringThread tmt) {
		return new TMPgcdTask(10, 1, tmt);
	}

	/**
	 * 10 times bigger than tiny.
	 */
	public static TMPgcdTask small(TickMeasuringThread tmt) {
		return new TMPgcdTask(100, 1, tmt);
	}

	/**
	 * 10 times bigger than small.
	 */
	public static TMPgcdTask medium(TickMeasuringThread tmt) {
		return new TMPgcdTask(100, 10, tmt);
	}

	/**
	 * 10 times bigger than medium.
	 */
	public static TMPgcdTask big(TickMeasuringThread tmt) {
		return new TMPgcdTask(100, 100, tmt);
	}

	/**
	 * 10 times bigger than big.
	 */
	public static TMPgcdTask bigger(TickMeasuringThread tmt) {
		return new TMPgcdTask(1000, 100, tmt);
	}

	/**
	 * 10 times bigger than bigger.
	 */
	public static TMPgcdTask huge(TickMeasuringThread tmt) {
		return new TMPgcdTask(1000, 1000, tmt);
	}

	@Override
	public void run() {
		for (int i = 0; i < this.i; i++) {
			for (int j = 0; j < this.j; j++) {
				pgcd(i, j);
			}
		}
		super.run();//signals task completion
	}

}
