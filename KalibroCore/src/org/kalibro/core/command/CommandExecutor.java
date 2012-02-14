package org.kalibro.core.command;

import java.io.IOException;
import java.io.InputStream;

public class CommandExecutor {

	private String command;
	private ProcessStreamLogger processStreamLogger;

	public CommandExecutor(String command) {
		this.command = command;
		this.processStreamLogger = new FileProcessStreamLogger();
	}

	public InputStream executeCommandAndGetOuput() {
		Process process = executeCommandAndGetProcess();
		processStreamLogger.logErrorStream(process, command);
		return process.getInputStream();
	}

	public void executeCommandWithTimeout(long timeout) {
		Process process = executeCommandAndGetProcess();
		processStreamLogger.logErrorStream(process, command);
		processStreamLogger.logOutputStream(process, command);
		new WaitProcessTask(process).executeAndWait(timeout);
		if (process.exitValue() != 0)
			throw new RuntimeException("Command {" + command + "} returned with error status");
	}

	private Process executeCommandAndGetProcess() {
		try {
			return Runtime.getRuntime().exec(command);
		} catch (IOException exception) {
			throw new RuntimeException("Command {" + command + "} could not be executed", exception);
		}
	}
}