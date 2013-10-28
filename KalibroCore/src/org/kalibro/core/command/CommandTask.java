package org.kalibro.core.command;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.kalibro.KalibroException;
import org.kalibro.core.concurrent.VoidTask;

/**
 * Executes system call commands.
 * 
 * @author Carlos Morais
 */
public class CommandTask extends VoidTask {

	private String command;
	private File workingDirectory;

	public CommandTask(String command) {
		this(command, null);
	}

	public CommandTask(String command, File workingDirectory) {
		this.command = command;
		this.workingDirectory = workingDirectory;
	}

	public InputStream executeAndGetOuput() throws IOException {
		Process process = executeCommand();
		logStream(process.getErrorStream(), "err");
		return process.getInputStream();
	}

	public void executeWithInput(String input) throws IOException {
		Process process = executeCommand();
		logStream(process.getInputStream(), "out");
		logStream(process.getErrorStream(), "err");
		IOUtils.write(input, process.getOutputStream());
	}

	@Override
	protected void perform() throws Exception {
		Process process = executeCommand();
		logStream(process.getErrorStream(), "err");
		logStream(process.getInputStream(), "out");
		waitProcess(process);
		if (process.exitValue() != 0)
			throw new KalibroException("Command returned with error status: " + command);
	}

	private Process executeCommand() throws IOException {
		return Runtime.getRuntime().exec(command, null, workingDirectory);
	}

	private void logStream(InputStream inputStream, String fileExtension) {
		OutputStream outputStream = new CommandLogStream(command, fileExtension);
		new PipeTask(inputStream, outputStream).executeInBackground();
	}

	private void waitProcess(Process process) throws InterruptedException {
		try {
			process.waitFor();
		} finally {
			process.destroy();
		}
	}

	@Override
	public String toString() {
		return "executing command: " + command;
	}
}