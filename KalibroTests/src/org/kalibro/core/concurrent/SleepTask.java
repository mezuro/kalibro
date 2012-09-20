package org.kalibro.core.concurrent;

final class SleepTask extends VoidTask {

	private long sleepingTime;

	SleepTask(long sleepingTime) {
		this.sleepingTime = sleepingTime;
	}

	@Override
	protected void perform() throws InterruptedException {
		Thread.sleep(sleepingTime);
	}

	@Override
	public String toString() {
		return "sleeping for " + sleepingTime + " milliseconds.";
	}
}