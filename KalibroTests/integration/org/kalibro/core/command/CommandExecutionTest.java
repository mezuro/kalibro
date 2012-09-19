package org.kalibro.core.command;

import static org.junit.Assert.*;
import static org.kalibro.core.Environment.logsDirectory;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Test;
import org.kalibro.IntegrationTest;
import org.kalibro.core.concurrent.VoidTask;

public class CommandExecutionTest extends IntegrationTest {

	private static final long PIPE_TIMEOUT = 50;

	private CommandTask task;

	@After
	public void tearDown() {
		FileUtils.deleteQuietly(logsDirectory());
	}

	@Test
	public void shouldThrowExceptionWhenGettingOutputForInvalidCommand() {
		createCommandTask("invalid command");
		assertThat(new VoidTask() {

			@Override
			public void perform() throws IOException {
				task.executeAndGetOuput();
			}
		}).doThrow(IOException.class);
	}

	@Test
	public void shouldThrowExceptionWhenExecutingInvalidCommand() {
		createCommandTask("invalid command");
		assertThat(task).doThrow(IOException.class);
	}

	@Test
	public void shouldThrowExceptionOnBadExitValue() {
		createCommandTask("make etc");
		assertThat(task).throwsException().withMessage("Command returned with error status: make etc");
	}

	@Test
	public void shouldThrowExceptionOnCommandTimeout() {
		createCommandTask("sleep 1000");
		assertThat(new VoidTask() {

			@Override
			public void perform() {
				task.executeAndWait(50);
			}
		}).throwsException().withCause(InterruptedException.class)
			.withMessage("Timed out after 50 milliseconds while executing command: sleep 1000");
	}

	@Test
	public void shouldGetCommandOutput() throws IOException {
		createCommandTask("echo test");
		assertEquals("test\n", IOUtils.toString(task.executeAndGetOuput()));
	}

	@Test
	public void shouldLogCommandOutput() throws Exception {
		createCommandTask("echo test");
		task.executeAndWait();
		waitPipeTask();
		assertEquals("$ echo test\ntest\n", getLog("out"));
		assertEquals("$ echo test\n", getLog("err"));
	}

	@Test
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