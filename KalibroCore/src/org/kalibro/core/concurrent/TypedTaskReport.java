package org.kalibro.core.concurrent;

public class TypedTaskReport<T> extends TaskReport {

	private T result;

	protected TypedTaskReport(long executionTime, Exception error, T result) {
		super(executionTime, error);
		this.result = result;
	}

	public T getResult() {
		return result;
	}
}