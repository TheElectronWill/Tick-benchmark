package com.electronwill.tests.tasks;

/**
 *
 * @author TheElectronWill
 */
public class PgcdTask implements Runnable {

	static int pgcd(int a, int b) {
		return b == 0 ? a : pgcd(b, a % b);
	}

	private final int i, j;

	public PgcdTask(int i, int j) {
		this.i = i;
		this.j = j;
	}

	/**
	 * Mini task which takes almost 0 time to complete.
	 */
	public static PgcdTask mini() {
		return new PgcdTask(1, 1);
	}

	/**
	 * 100 times bigger than mini.
	 */
	public static PgcdTask small() {
		return new PgcdTask(50, 2);
	}

	/**
	 * 10 times bigger than small.
	 */
	public static PgcdTask medium() {
		return new PgcdTask(100, 10);
	}

	/**
	 * 10 times bigger than medium.
	 */
	public static PgcdTask big() {
		return new PgcdTask(100, 100);
	}

	/**
	 * 10 times bigger than big.
	 */
	public static PgcdTask bigger() {
		return new PgcdTask(1000, 100);
	}

	/**
	 * 10 times bigger than bigger.
	 */
	public static PgcdTask huge() {
		return new PgcdTask(1000, 1000);
	}

	@Override
	public void run() {
		for (int i = 0; i < this.i; i++) {
			for (int j = 0; j < this.j; j++) {
				pgcd(i, j);
			}
		}
	}

}
