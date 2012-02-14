package org.kalibro.core.concurrent;

public abstract class TypedTask<T> extends Task {

	protected T result;

	protected void setResult(T result) {
		this.result = result;
	}

	public T executeAndWaitResult(long timeout) {
		super.executeAndWait(timeout);
		return result;
	}

	@Override
	public void perform() throws Exception {
		setResult(generateResult());
	}

	public abstract T generateResult() throws Exception;

	@Override
	protected void reportTaskFinished(TaskReport report) {
		long executionTime = report.getExecutionTime();
		Exception error = report.getError();
		super.reportTaskFinished(new TypedTaskReport<T>(executionTime, error, result));
	}
}