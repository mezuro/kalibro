package org.kalibro.core.concurrent;

public class ThrowExceptionTask extends Task {

	@Override
	public void perform() throws Exception {
		throw new Exception();
	}
}