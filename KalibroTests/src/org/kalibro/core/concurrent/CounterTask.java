package org.kalibro.core.concurrent;

final class CounterTask extends Task<Integer> {

	int result;

	@Override
	public Integer compute() {
		return result++;
	}
}