package org.kalibro.core.concurrent;

import java.util.Timer;
import java.util.TimerTask;

class TaskExecutor {

	private Task task;
	private Timer periodicExecutionTimer;

	protected TaskExecutor(Task task) {
		this.task = task;
		this.periodicExecutionTimer = new Timer();
	}

	protected void executeInBackground() {
		new Thread(task).start();
	}

	protected void executeAndWait(long timeout) {
		Timer timer = new Timer();
		timer.schedule(new ThreadInterrupter(Thread.currentThread()), timeout);

		try {
			task.perform();
		} catch (InterruptedException exception) {
			throw new RuntimeException("Task timed out after " + timeout + " milliseconds.", exception);
		} catch (Exception exception) {
			throw new RuntimeException("Task did not end normally.", exception);
		} finally {
			timer.cancel();
		}
	}

	protected void executePeriodically(long period) {
		periodicExecutionTimer.schedule(new TimerTask() {

			@Override
			public void run() {
				task.run();
			}
		}, 0, period);
	}

	protected void cancelPeriodicExecution() {
		periodicExecutionTimer.cancel();
	}
}