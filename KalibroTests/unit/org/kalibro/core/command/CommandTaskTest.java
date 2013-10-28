package org.kalibro.core.command;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.tests.UnitTest;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({CommandTask.class, Runtime.class})
public class CommandTaskTest extends UnitTest {

	private static final String COMMAND = "CommandTaskTest command";

	private Runtime runtime;
	private Process process;
	private PipeTask errorPipe, normalPipe;
	private InputStream errorInput, normalInput;
	private CommandLogStream errorOutput, normalOutput;

	private CommandTask commandTask;

	@Before
	public void setUp() throws Exception {
		mockProcess();
		mockLogging();
		commandTask = new CommandTask(COMMAND);
	}

	private void mockProcess() throws IOException {
		runtime = mock(Runtime.class);
		process = mock(Process.class);
		errorInput = mock(InputStream.class);
		normalInput = mock(InputStream.class);

		mockStatic(Runtime.class);
		when(Runtime.getRuntime()).thenReturn(runtime);
		when(runtime.exec(COMMAND, null, null)).thenReturn(process);
		when(process.getErrorStream()).thenReturn(errorInput);
		when(process.getInputStream()).thenReturn(normalInput);
	}

	private void mockLogging() throws Exception {
		errorPipe = mock(PipeTask.class);
		normalPipe = mock(PipeTask.class);
		errorOutput = mock(CommandLogStream.class);
		normalOutput = mock(CommandLogStream.class);

		whenNew(CommandLogStream.class).withArguments(COMMAND, "err").thenReturn(errorOutput);
		whenNew(CommandLogStream.class).withArguments(COMMAND, "out").thenReturn(normalOutput);
		whenNew(PipeTask.class).withArguments(errorInput, errorOutput).thenReturn(errorPipe);
		whenNew(PipeTask.class).withArguments(normalInput, normalOutput).thenReturn(normalPipe);
	}

	@Test
	public void shouldExecuteCommandOnWorkingDirectory() throws Exception {
		File workingDirectory = mock(File.class);
		when(runtime.exec(COMMAND, null, workingDirectory)).thenReturn(process);

		new CommandTask(COMMAND, workingDirectory).executeAndGetOuput();
		verify(runtime).exec(COMMAND, null, workingDirectory);
	}

	@Test
	public void shouldExecuteAndGetOutput() throws IOException {
		assertSame(normalInput, commandTask.executeAndGetOuput());
		verify(errorPipe).executeInBackground();
	}

	@Test
	public void shouldExecuteWithInput() throws IOException {
		String input = "CommandTaskTest input";
		OutputStream outputStream = mock(OutputStream.class);
		when(process.getOutputStream()).thenReturn(outputStream);

		commandTask.executeWithInput(input);
		verify(normalPipe).executeInBackground();
		verify(errorPipe).executeInBackground();
		verify(outputStream).write(input.getBytes());
	}

	@Test
	public void shouldThrowExceptionWhenFailToCreateProcess() throws IOException {
		IOException exception = new IOException();
		when(runtime.exec(COMMAND, null, null)).thenThrow(exception);
		assertThat(commandTask).doThrow(exception);
	}

	@Test
	public void shouldLogProcessInputStreamsInBackground() throws Exception {
		commandTask.perform();
		verify(errorPipe).executeInBackground();
		verify(normalPipe).executeInBackground();
	}

	@Test
	public void shouldThrowExceptionWhenProcessTerminatesWithBadExitValue() {
		when(process.exitValue()).thenReturn(1);
		assertThat(commandTask).throwsException().withMessage("Command returned with error status: " + COMMAND);
	}

	@Test
	public void shouldDestroyProcessOnNormalExecution() throws Exception {
		commandTask.perform();
		verify(process).waitFor();
		verify(process).destroy();
	}

	@Test
	public void shouldDestroyProcessOnInterruptedWainting() throws InterruptedException {
		InterruptedException exception = new InterruptedException();
		when(process.waitFor()).thenThrow(exception);

		assertThat(commandTask).doThrow(exception);
		verify(process).destroy();
	}

	@Test
	public void shouldShowCommandOnDescription() {
		assertEquals("executing command: " + COMMAND, "" + commandTask);
	}
}