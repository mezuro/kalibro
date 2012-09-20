package org.kalibro.core.concurrent;

final class DoNothingTask extends VoidTask {

	@Override
	protected void perform() {
		return;
	}
}