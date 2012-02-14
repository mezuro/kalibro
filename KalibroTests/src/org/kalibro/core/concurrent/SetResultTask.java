package org.kalibro.core.concurrent;

public class SetResultTask<T> extends TypedTask<T> {

	private T resultToBeSet;

	public SetResultTask(T result) {
		resultToBeSet = result;
	}

	@Override
	public T generateResult() {
		return resultToBeSet;
	}
}