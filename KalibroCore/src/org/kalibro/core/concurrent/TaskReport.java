package org.kalibro.core.concurrent;

public class TaskReport {

	private long executionTime;
	private Throwable error;

	protected TaskReport(long executionTime, Throwable error) {
		this.executionTime = executionTime;
		this.error = error;
	}

	public boolean isTaskDone() {
		return error == null;
	}

	public long getExecutionTime() {
		return executionTime;
	}

	public Throwable getError() {
		return error;
	}
}