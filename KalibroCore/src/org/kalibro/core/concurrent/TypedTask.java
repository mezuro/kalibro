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
	protected void perform() throws Throwable {
		setResult(performAndGetResult());
	}

	protected abstract T performAndGetResult() throws Throwable;

	@Override
	protected void reportTaskFinished(TaskReport report) {
		long executionTime = report.getExecutionTime();
		Throwable error = report.getError();
		super.reportTaskFinished(new TypedTaskReport<T>(executionTime, error, result));
	}
}