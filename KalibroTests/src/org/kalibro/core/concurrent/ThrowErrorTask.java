package org.kalibro.core.concurrent;

final class ThrowErrorTask extends VoidTask {

	private Throwable error;

	ThrowErrorTask() {
		this(new Throwable());
	}

	ThrowErrorTask(Throwable error) {
		this.error = error;
	}

	@Override
	protected void perform() throws Throwable {
		throw error;
	}

	@Override
	public String toString() {
		return "throwing error.";
	}
}