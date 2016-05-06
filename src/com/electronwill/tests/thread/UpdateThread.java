/*
 * Copyright (C) 2015 TheElectronWill
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General
 * Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Affero General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package com.electronwill.tests.thread;

import java.util.concurrent.locks.LockSupport;

/**
 *
 * @author TheElectronWill
 */
public final class UpdateThread extends NiceThread {

	final Bag<Runnable> toUpdate;
	volatile long tickDuration = 0;// in nanoseconds
	final int id;

	UpdateThread(int id) {
		this(id, new SimpleBag<>(200, 10));
	}

	UpdateThread(int id, Bag<Runnable> updateables) {
		this.toUpdate = updateables;
		this.id = id;
	}

	@Override
	public void doRun() {
		while (run) {// Main loop. 1 tick = 50 ms = 50 000 000 ns
			long t0 = System.nanoTime();// Initial time in nanoseconds.

			// == Game update ==
			for (int i = 0; i < toUpdate.size(); i++) {
				Runnable u = toUpdate.get(i);
				try {
					u.run();
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			long t1 = System.nanoTime();
			tickDuration = t1 - t0;

			// == Wait for the next tick ==
			long timeElapsed;
			while ((timeElapsed = System.nanoTime() - t0) < 50_000_000) {
				// == Wait ==
				if (timeElapsed < 42_000_000) {// time left > 8ms
					LockSupport.parkNanos(5_000_000);// parks for 5ms
				}
			}
		}
	}

}
