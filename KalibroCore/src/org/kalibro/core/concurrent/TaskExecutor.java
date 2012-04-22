package org.kalibro.core.concurrent;

import java.util.Timer;
import java.util.TimerTask;

import org.kalibro.KalibroException;

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

	protected void executeAndWait() {
		task.run();
		checkTaskReport(0);
	}

	protected void executeAndWait(long timeout) {
		Timer timer = new Timer();
		timer.schedule(new ThreadInterrupter(Thread.currentThread()), timeout);
		task.run();
		timer.cancel();
		checkTaskReport(timeout);
	}

	private void checkTaskReport(long timeout) {
		Throwable error = task.getReport().getError();
		if (error instanceof KalibroException)
			throw (KalibroException) error;
		if (error instanceof InterruptedException)
			throw new KalibroException("Timed out after " + timeout + " milliseconds while " + task, error);
		if (error != null)
			throw new KalibroException("Error while " + task, error);
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