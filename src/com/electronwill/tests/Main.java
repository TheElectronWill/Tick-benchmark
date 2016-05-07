package com.electronwill.tests;

import com.electronwill.tests.tasks.ClockTask;
import com.electronwill.tests.tasks.TMPgcdTask;
import com.electronwill.tests.tasks.TaskCreator;
import com.electronwill.tests.tasks.TickMeasuringThread;
import com.electronwill.tests.thread.ConcurrentUpdater;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author TheElectronWill
 */
public class Main {

	public static void main(String[] args) throws InterruptedException {
		String taskName;//type de tâche
		int nTasks;//nombre de tâches
		int nTicks;//nombre de ticks
		boolean testExecutor = true, testThread = true;
		switch (args.length) {
			case 0:
				Scanner sc = new Scanner(System.in);
				System.out.println("Type de tâche ? (counter/pmini/ptiny/psmall/pmedium/pbig/pbigger/phuge/uneven/help/debug)");
				taskName = sc.nextLine();
				if (!doAction(taskName)) {
					return;
				}
				System.out.println("Nombre de tâches par tick ?");
				nTasks = Integer.parseInt(sc.nextLine());
				System.out.println("Nombre de ticks ?");
				nTicks = Integer.parseInt(sc.nextLine());
				break;
			case 1:
				taskName = args[0];
				if (doAction(taskName)) {
					System.out.println("Paramètres invalides ou insuffisants. Utilisez le paramètre \"help\" pour voir l'aide.");
				}
				return;
			case 3:
			case 4:
				taskName = args[0];
				nTasks = Integer.parseInt(args[1]);
				nTicks = Integer.parseInt(args[2]);
				if (args.length == 4) {
					if (args[0].equalsIgnoreCase("executorOnly") || args[0].equalsIgnoreCase("onlyExecutor")) {
						testThread = false;
					} else if (args[0].equalsIgnoreCase("threadOnly") || args[0].equalsIgnoreCase("onlyThread")) {
						testExecutor = false;
					} else {
						System.out.println("Paramètre invalide : \"" + args[0] + "\"");
					}
				}
				break;
			default:
				System.out.println("Nombre de paramètres invalide. Utilisez le paramètre \"help\" pour voir l'aide.");
				return;
		}
		System.out.println("Lancement du test...");
		if (taskName.equalsIgnoreCase("uneven")) {
			benchmarkUneven(nTasks, nTicks, testExecutor, testThread);
		} else {
			benchmark(taskName, nTasks, nTicks, testExecutor, testThread);
		}
	}

	/**
	 * @return true if it needs more parameters, false otherwise.
	 */
	static boolean doAction(String actionName) throws InterruptedException {
		if (actionName.equalsIgnoreCase("help") || actionName.equalsIgnoreCase("--help")) {
			printHelp();
		} else if (actionName.equalsIgnoreCase("debug") || actionName.equalsIgnoreCase("--debug")) {
			debug();
		} else {
			return true;
		}
		return false;
	}

	static void benchmark(String taskName, int nTasks, int nTicks, boolean testExecutor, boolean testThread) throws InterruptedException {
		new ConsoleThread().start();
		System.out.println("----------------------------------------------");
		System.out.println("-             Phase de \"warm up\"             -");
		System.out.println("----------------------------------------------");
		System.out.println("Durant cette phase, des statistiques seront affichées, mais elles seront probablement moins bonnes car la JVM n'aura pas encore fait toutes les compilations et optimisations utiles.");
		if (testExecutor) {
			benchmarkScheduledExecutor(taskName, nTasks, 200);
			System.gc();
		}
		if (testThread) {
			benchmarkUpdateThread(taskName, nTasks, 200);
			System.gc();
		}

		System.out.println("----------------------------------------------");
		System.out.println("-                Benchmark !                 -");
		System.out.println("----------------------------------------------");
		if (testExecutor) {
			benchmarkScheduledExecutor(taskName, nTasks, nTicks);
			System.gc();
		}
		if (testThread) {
			benchmarkUpdateThread(taskName, nTasks, nTicks);
		}
		System.exit(0);
	}

	static void benchmarkScheduledExecutor(String taskName, int nTasks, int nTicks) throws InterruptedException {
		System.out.println("========== ScheduledExecutorService ==========");
		System.out.println("Type de tâche : " + taskName);
		System.out.println("Nombre de tâches par tick : " + nTasks);
		System.out.println("Nombre de ticks à effectuer : " + nTicks);
		System.out.println("Exécution des ticks...");
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(4);

		TickMeasuringThread tmt = new TickMeasuringThread(nTasks, nTicks, executor, null, 0, false);
		tmt.start();
		TaskCreator.setTmt(tmt);
		Runnable task = TaskCreator.createFromName(taskName);

		for (int i = 0; i < nTasks; i++) {
			executor.scheduleAtFixedRate(task, 0, 50, TimeUnit.MILLISECONDS);
		}
		executor.awaitTermination(10, TimeUnit.DAYS);
		System.out.println();
	}

	static void benchmarkUpdateThread(String taskName, int nTasks, int nTicks) throws InterruptedException {
		System.out.println("================ UpdateThread ================");
		System.out.println("Type de tâche : " + taskName);
		System.out.println("Nombre de tâches par tick : " + nTasks);
		System.out.println("Nombre de ticks à effectuer : " + nTicks);
		System.out.println("Exécution des ticks...");
		ConcurrentUpdater updater = new ConcurrentUpdater(4);

		TickMeasuringThread tmt = new TickMeasuringThread(nTasks, nTicks, null, updater, 0, false);
		tmt.start();
		TaskCreator.setTmt(tmt);
		Runnable task = TaskCreator.createFromName(taskName);

		for (int i = 0; i < nTasks; i++) {
			updater.submit(task);
		}
		updater.start();
		updater.awaitTermination();
		System.out.println();
	}

	static void benchmarkUneven(int nTasks, int nTicks, boolean testExecutor, boolean testThread) throws InterruptedException {
		new ConsoleThread().start();
		System.out.println("----------------------------------------------");
		System.out.println("-             Phase de \"warm up\"             -");
		System.out.println("----------------------------------------------");
		System.out.println("Durant cette phase, des statistiques seront affichées, mais elles seront probablement moins bonnes car la JVM n'aura pas encore fait toutes les compilations et optimisations utiles.");
		if (testExecutor) {
			benchmarkUnevenScheduledExecutor(nTasks, 200);
			System.gc();
		}
		if (testThread) {
			benchmarkUnevenUpdateThread(nTasks, 200);
			System.gc();
		}

		System.out.println("----------------------------------------------");
		System.out.println("-                Benchmark !                 -");
		System.out.println("----------------------------------------------");
		if (testExecutor) {
			benchmarkUnevenScheduledExecutor(nTasks, nTicks);
			System.gc();
		}
		if (testThread) {
			benchmarkUnevenUpdateThread(nTasks, nTicks);
		}
		System.exit(0);
	}

	static void benchmarkUnevenUpdateThread(int nTasks, int nTicks) throws InterruptedException {
		nTasks = (nTasks / 4) * 4;
		System.out.println("================ UpdateThread ================");
		System.out.println("Type de tâche : ptiny et psmall (mauvaise répartition : 1 thread sur 4 sera plus chargé que les autres)");
		System.out.println("Nombre de tâches par tick : " + nTasks);
		System.out.println("Nombre de ticks à effectuer : " + nTicks);
		System.out.println("Exécution des ticks...");
		ConcurrentUpdater updater = new ConcurrentUpdater(4);

		TickMeasuringThread tmt = new TickMeasuringThread(nTasks, nTicks, null, updater, 0, false);
		tmt.start();
		Runnable tinyTask = TMPgcdTask.tiny(tmt), smallTask = TMPgcdTask.small(tmt);

		for (int i = 0; i < nTasks / 4; i++) {
			updater.submit(tinyTask);
			updater.submit(tinyTask);
			updater.submit(tinyTask);
			updater.submit(smallTask);
		}
		updater.start();
		updater.awaitTermination();
		System.out.println();
	}

	static void benchmarkUnevenScheduledExecutor(int nTasks, int nTicks) throws InterruptedException {
		nTasks = (nTasks / 4) * 4;
		System.out.println("================ ScheduledExecutor ================");
		System.out.println("Type de tâche : ptiny et psmall");
		System.out.println("Nombre de tâches par tick : " + nTasks);
		System.out.println("Nombre de ticks à effectuer : " + nTicks);
		System.out.println("Exécution des ticks...");
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(4);

		TickMeasuringThread tmt = new TickMeasuringThread(nTasks, nTicks, executor, null, 0, false);
		tmt.start();
		Runnable tinyTask = TMPgcdTask.tiny(tmt), smallTask = TMPgcdTask.small(tmt);

		for (int i = 0; i < nTasks / 4; i++) {
			executor.scheduleAtFixedRate(tinyTask, 0, 50, TimeUnit.MILLISECONDS);
			executor.scheduleAtFixedRate(tinyTask, 0, 50, TimeUnit.MILLISECONDS);
			executor.scheduleAtFixedRate(tinyTask, 0, 50, TimeUnit.MILLISECONDS);
			executor.scheduleAtFixedRate(smallTask, 0, 50, TimeUnit.MILLISECONDS);
		}
		executor.awaitTermination(10, TimeUnit.DAYS);
		System.out.println();
	}

	static void debug() throws InterruptedException {
		debugScheduledExecutor();
		debugUpdateThread();
		System.exit(0);
	}

	static void debugScheduledExecutor() throws InterruptedException {
		System.out.println("========== ScheduledExecutorService (debug) ==========");
		new ConsoleThread().start();
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(4);
		executor.scheduleAtFixedRate(new ClockTask(), 0, 50, TimeUnit.MILLISECONDS);
		Thread.sleep(2000);
		executor.shutdownNow();
	}

	static void debugUpdateThread() throws InterruptedException {
		System.out.println("================ UpdateThread (debug) ================");
		new ConsoleThread().start();
		ConcurrentUpdater updater = new ConcurrentUpdater(4);
		updater.submit(new ClockTask());
		updater.start();
		Thread.sleep(2000);
		updater.stopNow();
	}

	static void printHelp() {
		System.out.println("================ Aide ================");
		System.out.println("Paramètres possibles : [help|debug| {counter|pmini|ptiny|psmall|pmedium|pbig|pbigger|phuge|uneven} param ]");
		System.out.println("  help : affiche cette aide");
		System.out.println("  debug : vérifie que les ticks s'exécutent toutes les 50 millisecondes");
		System.out.println("  {counter|pmini|ptiny|psmall|pmedium|pbig|pbigger|phuge|uneven} param : exécute un benchmark avec le type de tâche spécifié");
		System.out.println("  <aucun> : demande les paramètres nécessaires un par un");
		System.out.println("Paramètres du benchmark : nTasks nTicks [executorOnly|threadOnly]");
		System.out.println("  nTasks : nombre de tâches par tick");
		System.out.println("  nTicks : nombre de ticks à effectuer au total");
		System.out.println("  executorOnly : ne teste que le ScheduledExecutor");
		System.out.println("  threadOnly : ne teste que l'UpdateThread");
	}

}
