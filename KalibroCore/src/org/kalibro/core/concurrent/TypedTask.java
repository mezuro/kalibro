package org.kalibro.core.concurrent;

public abstract class TypedTask<T> extends Task {

	private T result;

	protected void setResult(T result) {
		this.result = result;
	}

	public T executeAndWaitResult() {
		super.executeAndWait();
		return result;
	}

	public T executeAndWaitResult(long timeout) {
		super.executeAndWait(timeout);
		return result;
	}

	@Override
	public void perform() throws Throwable {
		setResult(compute());
	}

	protected abstract T compute() throws Throwable;

	@Override
	protected void setReport(TaskReport<?> report) {
		this.report = report;
		if (report.isTaskDone())
			this.report = new TaskReport<T>(this, System.currentTimeMillis() - report.getExecutionTime(), result);
	}
}