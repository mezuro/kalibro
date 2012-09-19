package org.kalibro.core.concurrent;

class SleepTask extends VoidTask {

	private long sleepingTime;

	protected SleepTask(long sleepingTime) {
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