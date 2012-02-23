package org.kalibro.core.concurrent;

class SleepTask extends Task {

	private long sleepingTime;

	protected SleepTask(long sleepingTime) {
		this.sleepingTime = sleepingTime;
	}

	@Override
	public void perform() throws InterruptedException {
		Thread.sleep(sleepingTime);
	}
}