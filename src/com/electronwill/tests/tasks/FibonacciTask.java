package com.electronwill.tests.tasks;

/**
 *
 * @author TheElectronWill
 */
public class FibonacciTask implements Runnable {

	private final int n;

	public FibonacciTask(int n) {
		this.n = n;
	}

	/**
	 * Mini task which takes almost 0 time to complete.
	 */
	public static FibonacciTask mini() {
		return new FibonacciTask(1);
	}

	/**
	 * 10 times bigger than mini.
	 */
	public static FibonacciTask small() {
		return new FibonacciTask(10);
	}

	/**
	 * 10 times bigger than small.
	 */
	public static FibonacciTask medium() {
		return new FibonacciTask(100);
	}

	/**
	 * 10 times bigger than medium.
	 */
	public static FibonacciTask big() {
		return new FibonacciTask(1_000);
	}

	/**
	 * 10 times bigger than big.
	 */
	public static FibonacciTask bigger() {
		return new FibonacciTask(10_000);
	}

	/**
	 * 10 times bigger than bigger.
	 */
	public static FibonacciTask huge() {
		return new FibonacciTask(100_000);
	}

	@Override
	public void run() {
		for (int i = 0; i < n; i++) {
			fibonacci(40);
		}
	}

	private static int fibonacci(int n) {
		int a = 1, b = 1;
		for (int i = 0; i < n; i++) {
			int next = a + b;
			a = b;
			b = next;
		}
		return b;
	}

}
