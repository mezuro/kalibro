package org.kalibro.core.concurrent;

class ThrowExceptionTask extends Task {

	@Override
	public void perform() throws Exception {
		throw new Exception();
	}
}