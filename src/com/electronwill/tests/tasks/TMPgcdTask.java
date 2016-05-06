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

	public TMPgcdTask(int i, int j) {
		this.i = i;
		this.j = j;
	}

	/**
	 * Mini task which takes almost 0 time to complete.
	 */
	public static TMPgcdTask mini() {
		return new TMPgcdTask(1, 1);
	}

	/**
	 * 100 times bigger than mini.
	 */
	public static TMPgcdTask small() {
		return new TMPgcdTask(100, 1);
	}

	/**
	 * 10 times bigger than small.
	 */
	public static TMPgcdTask medium() {
		return new TMPgcdTask(100, 10);
	}

	/**
	 * 10 times bigger than medium.
	 */
	public static TMPgcdTask big() {
		return new TMPgcdTask(100, 100);
	}

	/**
	 * 10 times bigger than big.
	 */
	public static TMPgcdTask bigger() {
		return new TMPgcdTask(1000, 100);
	}

	/**
	 * 10 times bigger than bigger.
	 */
	public static TMPgcdTask huge() {
		return new TMPgcdTask(1000, 1000);
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
