package org.kalibro.core.concurrent;

import java.util.TimerTask;

public class ThreadInterrupter extends TimerTask {

	private Thread thread;

	protected ThreadInterrupter(Thread thread) {
		this.thread = thread;
	}

	@Override
	public void run() {
		thread.interrupt();
	}
}