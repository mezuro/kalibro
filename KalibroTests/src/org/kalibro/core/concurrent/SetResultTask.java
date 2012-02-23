package org.kalibro.core.concurrent;

class SetResultTask<T> extends TypedTask<T> {

	private T resultToBeSet;

	protected SetResultTask(T result) {
		resultToBeSet = result;
	}

	@Override
	public T generateResult() {
		return resultToBeSet;
	}
}