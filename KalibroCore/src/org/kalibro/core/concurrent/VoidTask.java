package org.kalibro.core.concurrent;

/**
 * This class exists as convenience for creating void tasks (those which do not produce result) without the ugliness of
 * a {@link Void} return type and a {@code return null;} statement.
 * 
 * @author Carlos Morais
 */
public abstract class VoidTask extends Task<Void> {

	@Override
	protected final Void compute() throws Throwable {
		perform();
		return null;
	}

	protected abstract void perform() throws Throwable;
}