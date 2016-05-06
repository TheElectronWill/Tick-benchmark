package com.electronwill.tests.thread;

/**
 *
 * @author TheElectronWill
 */
public abstract class NiceThread implements Runnable {

	protected volatile boolean run = true;
	protected volatile Thread t;
	protected final String name;

	public NiceThread() {
		this.name = null;
	}

	public NiceThread(String name) {
		this.name = name;
	}

	public void awaitTermination() {
		try {
			t.join();
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}

	public void start() {
		t = (name == null) ? new Thread(this) : new Thread(this, name);
		t.start();
	}

	public void stopNow() {
		run = false;
		try {
			t.stop();
		} catch (ThreadDeath ex) {
			//silence
		}
	}

	public void stopNicely() {
		run = false;
	}

	public boolean shouldRun() {
		return run;
	}

	@Override
	public void run() {
		try {
			doRun();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	protected abstract void doRun() throws Exception;

}
