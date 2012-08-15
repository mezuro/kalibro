package org.kalibro.core.command;

import static org.junit.Assert.*;
import static org.kalibro.Environment.*;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Test;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.concurrent.Task;

public class CommandExecutionTest extends KalibroTestCase {

	private static final long PIPE_TIMEOUT = 50;

	private CommandTask task;

	@After
	public void tearDown() {
		FileUtils.deleteQuietly(logsDirectory());
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void shouldThrowExceptionWhenGettingOutputForInvalidCommand() {
		createCommandTask("invalid command");
		checkException(new Task() {

			@Override
			public void perform() throws IOException {
				task.executeAndGetOuput();
			}
		}, IOException.class);
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void shouldThrowExceptionWhenExecutingInvalidCommand() {
		createCommandTask("invalid command");
		checkKalibroException(task, "Error while executing command: invalid command", IOException.class);
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void shouldThrowExceptionOnBadExitValue() {
		createCommandTask("make etc");
		checkKalibroException(task, "Command returned with error status: make etc");
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void shouldThrowExceptionOnCommandTimeout() {
		createCommandTask("sleep 1000");
		checkKalibroException(new Task() {

			@Override
			public void perform() {
				task.executeAndWait(50);
			}
		}, "Timed out after 50 milliseconds while executing command: sleep 1000", InterruptedException.class);
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void shouldGetCommandOutput() throws IOException {
		createCommandTask("echo test");
		assertEquals("test\n", IOUtils.toString(task.executeAndGetOuput()));
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void shouldLogCommandOutput() throws Exception {
		createCommandTask("echo test");
		task.executeAndWait();
		waitPipeTask();
		assertEquals("$ echo test\ntest\n", getLog("out"));
		assertEquals("$ echo test\n", getLog("err"));
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void shouldLogCommandErrorOutput() throws Exception {
		createCommandTask("make etc");
		executeErrorCommandQuietly();
		assertEquals("$ make etc\n", getLog("out"));
		assertTrue(getLog("err").startsWith("$ make etc\nmake: *** "));
	}

	private void executeErrorCommandQuietly() throws InterruptedException {
		try {
			task.executeAndWait();
			fail("Should have thrown exception");
		} catch (RuntimeException exception) {
			waitPipeTask();
		}
	}

	private void waitPipeTask() throws InterruptedException {
		Thread.sleep(PIPE_TIMEOUT);
	}

	private String getLog(String logFileExtension) throws IOException {
		File logFile = (File) FileUtils.iterateFiles(logsDirectory(), new String[]{logFileExtension}, false).next();
		return FileUtils.readFileToString(logFile);
	}

	private void createCommandTask(String command) {
		task = new CommandTask(command);
	}
}