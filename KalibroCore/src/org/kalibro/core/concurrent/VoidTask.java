package org.kalibro.core.concurrent;

public abstract class VoidTask extends Task<Void> {

	@Override
	protected Void compute() throws Throwable {
		perform();
		return null;
	}

	protected abstract void perform() throws Throwable;
}