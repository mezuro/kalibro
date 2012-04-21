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
	protected void setReport(long executionTime, Throwable error) {
		report = new TypedTaskReport<T>(executionTime, error, result);
	}
}