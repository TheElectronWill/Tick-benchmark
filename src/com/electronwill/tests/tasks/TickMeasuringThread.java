package com.electronwill.tests.tasks;

import com.electronwill.tests.thread.NiceThread;
import java.util.DoubleSummaryStatistics;
import java.util.concurrent.CountDownLatch;
import java.util.stream.DoubleStream;

/**
 *
 * @author TheElectronWill
 */
public class TickMeasuringThread extends NiceThread {

	CountDownLatch latch;
	final int nTasks;
	volatile boolean tickFinished = true;
	volatile long t0;
	final DoubleStream.Builder builder = DoubleStream.builder();

	public TickMeasuringThread(int nTasks) {
		super("TickMeasuringThread");
		this.nTasks = nTasks;
		this.latch = new CountDownLatch(nTasks);
	}

	@Override
	public void doRun() throws InterruptedException {
		Runtime.getRuntime().addShutdownHook(new PrintAverage());
		while (run) {
			latch.await();
			long t1 = System.nanoTime();

			tickFinished = true;
			latch = new CountDownLatch(nTasks);

			double ms = (t1 - t0) / 1000_000.0;
			System.out.println("tick : " + ms + " millisecondes");
		}
	}

	private class PrintAverage extends Thread {

		@Override
		public void run() {
			run = false;
			try {
				Thread.sleep(100);
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
			DoubleStream stream = builder.build().skip(3);
			AdvancedStatistics stats = new AdvancedStatistics();
			stream.forEach(stats::accept);
			System.out.println("---------- Stats ----------");
			System.out.println("Les 3 premières mesures ne comptent pas.");
			System.out.println("Nombre de mesures prises en compte : " + stats.getCount());
			System.out.println("Durée d'un tick :");
			System.out.println("moyenne : " + stats.getAverage());
			System.out.println("maximum : " + stats.getMax());
			System.out.println("minimum : " + stats.getMin());
			System.out.println("écart-type : " + stats.getStandardDeviation());
		}
	}

	private class AdvancedStatistics extends DoubleSummaryStatistics {

		private double sumOfSquares = 0;

		@Override
		public void accept(double value) {
			super.accept(value);
			sumOfSquares += (value * value);
		}

		public double getVariance() {
			return sumOfSquares / getCount() - (getAverage() * getAverage());
		}

		public double getStandardDeviation() {
			return Math.sqrt(getVariance());
		}

	}

}
