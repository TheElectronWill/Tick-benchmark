package com.electronwill.tests;

import java.util.Scanner;

/**
 *
 * @author TheElectronWill
 */
public class ConsoleThread extends Thread {

	@Override
	public void run() {
		Scanner sc = new Scanner(System.in);
		while (true) {
			String line = sc.nextLine();
			if (line.equalsIgnoreCase("stop") || line.equalsIgnoreCase("exit")) {
				System.exit(0);
			}
		}
	}

}
