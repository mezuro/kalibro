package org.kalibro.core.concurrent;

public class TypedTaskReport<T> extends TaskReport {

	private T result;

	public TypedTaskReport(long executionTime, Exception error, T result) {
		super(executionTime, error);
		this.result = result;
	}

	public T getResult() {
		return result;
	}
}