package org.kalibro.core.command;

import org.kalibro.core.concurrent.Task;

public class WaitProcessTask extends Task {

	private Process process;

	protected WaitProcessTask(Process process) {
		this.process = process;
	}

	@Override
	public void perform() throws InterruptedException {
		try {
			process.waitFor();
		} catch (InterruptedException exception) {
			process.destroy();
			throw exception;
		}
	}
}