package org.kalibro.core.concurrent;

class ThrowErrorTask extends VoidTask {

	private Throwable error;

	protected ThrowErrorTask() {
		this(new Throwable());
	}

	protected ThrowErrorTask(Throwable error) {
		this.error = error;
	}

	@Override
	public void perform() throws Throwable {
		throw error;
	}

	@Override
	public String toString() {
		return "throwing error";
	}
}