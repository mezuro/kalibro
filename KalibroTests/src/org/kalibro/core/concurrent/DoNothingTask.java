package org.kalibro.core.concurrent;

class DoNothingTask extends VoidTask {

	@Override
	public void perform() {
		return;
	}
}