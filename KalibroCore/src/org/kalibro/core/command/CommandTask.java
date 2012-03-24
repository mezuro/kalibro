package org.kalibro.core.command;

import java.io.IOException;
import java.io.InputStream;

import org.kalibro.KalibroException;
import org.kalibro.core.concurrent.Task;

public class CommandTask extends Task {

	private String command;
	private ProcessStreamLogger processStreamLogger;

	public CommandTask(String command) {
		this.command = command;
		this.processStreamLogger = new FileProcessStreamLogger();
	}

	public InputStream executeAndGetOuput() throws IOException {
		Process process = Runtime.getRuntime().exec(command);
		processStreamLogger.logErrorStream(process, command);
		return process.getInputStream();
	}

	@Override
	public void perform() throws Exception {
		Process process = Runtime.getRuntime().exec(command);
		processStreamLogger.logErrorStream(process, command);
		processStreamLogger.logOutputStream(process, command);
		waitProcess(process);
		if (process.exitValue() != 0)
			throw new KalibroException("Command returned with error status: " + command);
	}

	private void waitProcess(Process process) throws InterruptedException {
		try {
			process.waitFor();
		} catch (InterruptedException exception) {
			process.destroy();
			throw exception;
		}
	}

	@Override
	public String toString() {
		return "executing command: " + command;
	}
}