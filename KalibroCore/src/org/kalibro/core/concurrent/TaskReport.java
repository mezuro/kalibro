package org.kalibro.core.concurrent;

public class TaskReport {

	private long executionTime;
	private Exception error;

	public TaskReport(long executionTime, Exception error) {
		this.executionTime = executionTime;
		this.error = error;
	}

	public boolean isTaskDone() {
		return error == null;
	}

	public long getExecutionTime() {
		return executionTime;
	}

	public Exception getError() {
		return error;
	}
}