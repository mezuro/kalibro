package org.kalibro.core.command;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.kalibro.KalibroException;
import org.kalibro.core.concurrent.VoidTask;

public class CommandTask extends VoidTask {

	private String command;
	private File workingDirectory;
	private FileProcessStreamLogger processStreamLogger;

	public CommandTask(String command) {
		this(command, null);
	}

	public CommandTask(String command, File workingDirectory) {
		this.command = command;
		this.workingDirectory = workingDirectory;
		this.processStreamLogger = new FileProcessStreamLogger();
	}

	public InputStream executeAndGetOuput() throws IOException {
		Process process = Runtime.getRuntime().exec(command, null, workingDirectory);
		processStreamLogger.logErrorStream(process, command);
		return process.getInputStream();
	}

	@Override
	protected void perform() throws Exception {
		Process process = Runtime.getRuntime().exec(command, null, workingDirectory);
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