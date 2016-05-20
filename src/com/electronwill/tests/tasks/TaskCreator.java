package com.electronwill.tests.tasks;

/**
 *
 * @author TheElectronWill
 */
public class TaskCreator {

	private static volatile TickMeasuringThread tmt;

	public static void setTmt(TickMeasuringThread tmt) {
		TaskCreator.tmt = tmt;
	}

	public static TickMeasuringThread getTmt() {
		return tmt;
	}

	public static Runnable createFromName(String name) {
		return createFromName(name, true);
	}

	public static Runnable createFromName(String name, boolean concurrent) {
		switch (name.toLowerCase()) {
			case "counter":
				return concurrent ? new TMCounterTask.Concurrent(tmt) : new TMCounterTask.Simple(tmt);
			case "p_mini":
			case "pmini":
				return TMPgcdTask.mini(tmt);
			case "p_tiny":
			case "ptiny":
				return TMPgcdTask.tiny(tmt);
			case "p_small":
			case "psmall":
				return TMPgcdTask.small(tmt);
			case "p_medium":
			case "pmedium":
				return TMPgcdTask.medium(tmt);
			case "p_big":
			case "pbig":
				return TMPgcdTask.big(tmt);
			case "p_bigger":
			case "pbigger":
				return TMPgcdTask.bigger(tmt);
			case "p_huge":
			case "phuge":
				return TMPgcdTask.huge(tmt);
			case "hash_map":
			case "hashmap":
			case "map":
				return new TMHashMapTask(tmt, concurrent);
			default:
				throw new IllegalArgumentException("Tâche inconnue : " + name);
		}
	}

	private TaskCreator() {
	}

}
