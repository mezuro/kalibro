package org.kalibro.core.concurrent;

final class SleepTask extends VoidTask {

	private long sleepingTime;

	SleepTask(long sleepingTime) {
		this.sleepingTime = sleepingTime;
	}

	@Override
	public void perform() throws InterruptedException {
		Thread.sleep(sleepingTime);
	}

	@Override
	public String toString() {
		return "sleeping";
	}
}