package org.kalibro.core.concurrent;

final class CounterTask extends Task<Integer> {

	int result;

	@Override
	protected Integer compute() {
		return result++;
	}
}