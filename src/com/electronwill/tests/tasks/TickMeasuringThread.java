package com.electronwill.tests.tasks;

import com.electronwill.tests.thread.ConcurrentUpdater;
import com.electronwill.tests.thread.NiceThread;
import java.util.DoubleSummaryStatistics;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.stream.DoubleStream;

/**
 *
 * @author TheElectronWill
 */
public class TickMeasuringThread extends NiceThread {

	CountDownLatch latch;
	volatile boolean tickFinished = true;
	volatile long t0;

	final DoubleStream.Builder builder = DoubleStream.builder();
	final int maxTicks;//number of ticks to measure
	final int nTasks;//number of tasks per tick

	private int ticksCount = 0;//current number of ticks
	private final ExecutorService executorService;
	private final ConcurrentUpdater concurrentUpdater;
	private final int skipRecords;
	private final boolean printTicks;

	public TickMeasuringThread(int maxTicks, int nTasks, ExecutorService executorService, ConcurrentUpdater concurrentUpdater, int skipRecords, boolean printTicks) {
		super("TickMeasuringThread");
		this.maxTicks = maxTicks;
		this.nTasks = nTasks;
		this.executorService = executorService;
		this.concurrentUpdater = concurrentUpdater;
		this.skipRecords = skipRecords;
		this.printTicks = printTicks;
		this.latch = new CountDownLatch(nTasks);
	}

	@Override
	public void doRun() throws InterruptedException {
		Runtime.getRuntime().addShutdownHook(new StatsPrinterThread());
		while (run) {
			latch.await();
			long t1 = System.nanoTime();
			long delta = t1 - t0;

			tickFinished = true;
			latch = new CountDownLatch(nTasks);

			double ms = delta / 1000_000.0;
			builder.add(ms);
			if (printTicks) {
				System.out.println("tick : " + ms + " millisecondes");
			}

			ticksCount++;
			if (ticksCount >= maxTicks) {
				System.out.println(ticksCount + " ticks ont été exécutés, comme prévu.");
				run = false;
				printStats();
				stopWorkers();
			}
		}
	}

	private void printStats() {
		DoubleStream stream = builder.build().skip(skipRecords);
		AdvancedStatistics stats = new AdvancedStatistics();
		stream.forEach(stats::accept);
		System.out.println("---------- Statistiques ----------");
		if (skipRecords == 1) {
			System.out.println("La première mesure ne compte pas.");
			System.out.println("Nombre de mesures prises en compte : " + stats.getCount());
		} else if (skipRecords > 1) {
			System.out.println("Les " + skipRecords + " premières mesures ne comptent pas.");
			System.out.println("Nombre de mesures prises en compte : " + stats.getCount());
		} else {
			System.out.println("Toutes les mesures on été prises en compte.");
			System.out.println("Nombre de mesures : " + stats.getCount());
		}
		System.out.println("Durée d'un tick :");
		System.out.println("  minimum : " + stats.getMin());
		System.out.println("  maximum : " + stats.getMax());
		System.out.println("  moyenne : " + stats.getAverage());
		System.out.println("  écart-type : " + stats.getStandardDeviation());
	}

	private void stopWorkers() {
		if (executorService != null) {
			executorService.shutdownNow();
		}
		if (concurrentUpdater != null) {
			concurrentUpdater.stopNow();
		}
	}

	private class StatsPrinterThread extends Thread {

		@Override
		public void run() {
			run = false;
			try {
				Thread.sleep(100);
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
			if (ticksCount < maxTicks) {
				stopWorkers();
				printStats();
			}
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
