package org.kalibro.core.concurrent;

public abstract class TypedTask<T> extends Task<T> {

	public T executeAndWaitResult() {
		super.executeAndWait();
		return report.getResult();
	}

	public T executeAndWaitResult(long timeout) {
		super.executeAndWait(timeout);
		return report.getResult();
	}
}