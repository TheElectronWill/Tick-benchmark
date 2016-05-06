package com.electronwill.tests.thread;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Fast pseudo-random boolean generator. It randomly generates an int and uses its 32 bits to return
 * 32 booleans: 1 is true, 0 is false. An generated int is only used once. A new one is generated when
 * needed.
 */
class RandomBoolean {

	ThreadLocalRandom localRandom = ThreadLocalRandom.current();

	public RandomBoolean() {
		n = localRandom.nextLong();
		bit = 0;
	}
	private long n; // generated number
	private int bit; // bit index

	boolean next() {
		if (bit == 64) {
			n = localRandom.nextLong();
			bit = 0;
		}
		return (n & (1 << bit++)) == 1;
	}

}
