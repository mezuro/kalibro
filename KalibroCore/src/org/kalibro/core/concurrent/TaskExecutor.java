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
		try {
			task.perform();
		} catch (KalibroException exception) {
			throw exception;
		} catch (Throwable exception) {
			throw new KalibroException("Error while " + task, exception);
		}
	}

	protected void executeAndWait(long timeout) {
		Timer timer = new Timer();
		timer.schedule(new ThreadInterrupter(Thread.currentThread()), timeout);
		try {
			task.perform();
		} catch (KalibroException exception) {
			throw exception;
		} catch (InterruptedException exception) {
			throw new KalibroException("Timed out after " + timeout + " milliseconds while " + task, exception);
		} catch (Throwable exception) {
			throw new KalibroException("Error while " + task, exception);
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