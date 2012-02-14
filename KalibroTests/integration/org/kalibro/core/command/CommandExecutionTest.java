package org.kalibro.core.command;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Test;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.concurrent.Task;
import org.kalibro.core.util.Directories;

public class CommandExecutionTest extends KalibroTestCase {

	private static final long COMMAND_TIMEOUT = 100;
	private static final long PIPE_TIMEOUT = 50;
	private static final File LOGS = Directories.logs();

	private CommandExecutor executor;

	@After
	public void tearDown() {
		FileUtils.deleteQuietly(LOGS);
		LOGS.mkdirs();
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void checkInvalidCommandError() {
		createCommandExecutor("invalid command");
		checkException(new Task() {

			@Override
			public void perform() {
				executor.executeCommandAndGetOuput();
			}
		}, RuntimeException.class, "Command {invalid command} could not be executed", IOException.class);
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void checkInvalidCommandErrorWithTimeout() {
		createCommandExecutor("invalid command");
		checkException(new Task() {

			@Override
			public void perform() {
				executor.executeCommandWithTimeout(COMMAND_TIMEOUT);
			}
		}, RuntimeException.class, "Command {invalid command} could not be executed", IOException.class);
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void checkBadExitValueError() {
		createCommandExecutor("make etc");
		checkException(new Task() {

			@Override
			public void perform() {
				executor.executeCommandWithTimeout(COMMAND_TIMEOUT);
			}
		}, RuntimeException.class, "Command {make etc} returned with error status");
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void checkTimeoutError() {
		createCommandExecutor("sleep 1000");
		checkException(new Task() {

			@Override
			public void perform() {
				executor.executeCommandWithTimeout(COMMAND_TIMEOUT);
			}
		}, RuntimeException.class, "Task timed out after " + COMMAND_TIMEOUT + " milliseconds.",
			InterruptedException.class);
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void shouldGetCommandOutput() throws IOException {
		createCommandExecutor("echo test");
		assertEquals("test\n", IOUtils.toString(executor.executeCommandAndGetOuput()));
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void shouldLogCommandOutput() throws Exception {
		createCommandExecutor("echo test");
		executor.executeCommandWithTimeout(COMMAND_TIMEOUT);
		waitPipeTask();
		assertEquals("$ echo test\ntest\n", getLog("out"));
		assertEquals("$ echo test\n", getLog("err"));
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void shouldLogCommandErrorOutput() throws Exception {
		createCommandExecutor("make etc");
		executeErrorCommandQuietly();
		assertEquals("$ make etc\n", getLog("out"));
		assertTrue(getLog("err").startsWith("$ make etc\nmake: *** "));
	}

	private void executeErrorCommandQuietly() throws InterruptedException {
		try {
			executor.executeCommandWithTimeout(COMMAND_TIMEOUT);
			fail("Should have thrown exception");
		} catch (RuntimeException exception) {
			waitPipeTask();
		}
	}

	private void waitPipeTask() throws InterruptedException {
		Thread.sleep(PIPE_TIMEOUT);
	}

	private String getLog(String logFileExtension) throws IOException {
		File logFile = (File) FileUtils.iterateFiles(LOGS, new String[]{logFileExtension}, false).next();
		return FileUtils.readFileToString(logFile);
	}

	private void createCommandExecutor(String command) {
		executor = new CommandExecutor(command);
	}
}