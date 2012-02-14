package org.kalibro.core.concurrent;

public class SleepTask extends Task {

	private long sleepingTime;

	public SleepTask(long sleepingTime) {
		this.sleepingTime = sleepingTime;
	}

	@Override
	public void perform() throws InterruptedException {
		Thread.sleep(sleepingTime);
	}
}