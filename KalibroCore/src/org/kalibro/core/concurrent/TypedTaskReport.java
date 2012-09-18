package org.kalibro.core.concurrent;

public class TypedTaskReport<T> extends TaskReport {

	private T result;

	protected TypedTaskReport(TypedTask<T> task, long executionTime, Throwable error, T result) {
		super(task, executionTime, error);
		this.result = result;
	}

	public T getResult() {
		return result;
	}
}