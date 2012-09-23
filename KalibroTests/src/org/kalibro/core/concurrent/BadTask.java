package org.kalibro.core.concurrent;

final class BadTask extends Task<Object> {

	@Override
	public void run() {
		throw new NullPointerException();
	}

	@Override
	protected Object compute() {
		return null;
	}

	@Override
	public String toString() {
		return "overriding run()";
	}
}