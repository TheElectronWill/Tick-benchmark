package com.electronwill.tests;

import com.electronwill.tests.tasks.CounterTask;
import com.electronwill.tests.tasks.FibonacciTask;
import com.electronwill.tests.tasks.PgcdTask;

/**
 *
 * @author TheElectronWill
 */
public class TestMisc {

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) throws InterruptedException {
		testAll();
		testAll();
		testAll();
		testAll();
		testAll();
	}

	static void test(Runnable r, String name) {
		long t0 = System.nanoTime();
		r.run();
		long t1 = System.nanoTime();
		long time = t1 - t0;
		System.out.println(name + " : " + time + " ns");
	}

	static void testAll() {
		test(new CounterTask(), "counter ");

		test(PgcdTask.mini(), "p_mini  ");
		test(PgcdTask.small(), "p_small ");
		test(PgcdTask.medium(), "p_medium");
		test(PgcdTask.big(), "p_big   ");
		test(PgcdTask.bigger(), "p_bigger");

		test(FibonacciTask.mini(), "f_mini  ");
		test(FibonacciTask.small(), "f_small ");
		test(FibonacciTask.medium(), "f_medium");
		test(FibonacciTask.big(), "f_big   ");
		test(FibonacciTask.bigger(), "f_bigger");
		test(FibonacciTask.huge(), "f_huge");
	}

}
