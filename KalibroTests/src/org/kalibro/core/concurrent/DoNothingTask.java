package org.kalibro.core.concurrent;

final class DoNothingTask extends VoidTask {

	@Override
	public void perform() {
		return;
	}
}