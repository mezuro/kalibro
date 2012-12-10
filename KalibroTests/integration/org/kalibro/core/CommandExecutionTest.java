package org.kalibro.core;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.Assert.*;
import static org.kalibro.core.Environment.logsDirectory;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.kalibro.core.command.CommandTask;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.tests.IntegrationTest;

public class CommandExecutionTest extends IntegrationTest {

	@Test
	public void shouldThrowExceptionWhenExecutingInvalidCommand() {
		CommandTask invalidCommand = command("invalid command");
		assertThat(invalidCommand).doThrow(IOException.class);
		assertThat(getOutputFor(invalidCommand)).doThrow(IOException.class);
	}

	private VoidTask getOutputFor(final CommandTask command) {
		return new VoidTask() {

			@Override
			protected void perform() throws IOException {
				command.executeAndGetOuput();
			}
		};
	}

	@Test
	public void shouldThrowExceptionOnBadExitValue() {
		assertThat(command("make etc")).throwsException().withMessage("Command returned with error status: make etc");
	}

	@Test
	public void shouldThrowExceptionOnCommandTimeout() {
		assertThat(command("sleep 1")).timesOutWith(50, MILLISECONDS);
	}

	@Test
	public void shouldGetCommandOutput() throws IOException {
		assertEquals("test\n", IOUtils.toString(command("echo test").executeAndGetOuput()));
	}

	@Test
	public void shouldLogCommandOutput() throws Exception {
		command("echo test").execute();
		waitLogging();
		assertEquals("\n\n$ echo test\ntest\n", getLog("echo", "out"));
	}

	@Test
	public void shouldNotLogInexistentOutput() throws Exception {
		assertThat(command("make etc")).throwsException();
		waitLogging();
		assertNull(getLog("make", "out"));
	}

	@Test
	public void shouldLogErrorOutput() throws Exception {
		assertThat(command("make etc")).throwsException();
		waitLogging();
		assertTrue(getLog("make", "err").startsWith("\n\n$ make etc\nmake: *** "));
	}

	@Test
	public void shouldNotLogInexistentErrorOutput() throws Exception {
		command("echo test").execute();
		waitLogging();
		assertNull(getLog("echo", "err"));
	}

	private CommandTask command(String command) {
		return new CommandTask(command);
	}

	private void waitLogging() throws InterruptedException {
		Thread.sleep(4000);
	}

	private String getLog(String name, String extension) throws IOException {
		String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		File logFile = new File(logsDirectory(), name + "." + today + "." + extension);
		return logFile.exists() ? FileUtils.readFileToString(logFile) : null;
	}
}