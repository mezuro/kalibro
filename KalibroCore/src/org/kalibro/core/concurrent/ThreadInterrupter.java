package org.kalibro.core.concurrent;

import java.util.TimerTask;

class ThreadInterrupter extends TimerTask {

	private Thread thread;

	protected ThreadInterrupter(Thread thread) {
		this.thread = thread;
	}

	@Override
	public void run() {
		thread.interrupt();
	}
}