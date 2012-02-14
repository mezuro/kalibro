package org.kalibro.core.command;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.concurrent.Task;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(CommandExecutor.class)
public class CommandExecutorTest extends KalibroTestCase {

	private String command;
	private Process process;
	private Runtime runtime;
	private InputStream output, error;
	private FileProcessStreamLogger logger;

	private CommandExecutor executor;

	@Before
	public void setUp() throws Exception {
		command = "my command";
		mockProcess();
		mockRuntime();
		mockLogger();
		executor = new CommandExecutor(command);
	}

	private void mockProcess() {
		output = PowerMockito.mock(InputStream.class);
		error = PowerMockito.mock(InputStream.class);
		process = PowerMockito.mock(Process.class);
		PowerMockito.when(process.getInputStream()).thenReturn(output);
		PowerMockito.when(process.getErrorStream()).thenReturn(error);
	}

	private void mockRuntime() {
		runtime = PowerMockito.mock(Runtime.class);
		PowerMockito.mockStatic(Runtime.class);
		PowerMockito.when(Runtime.getRuntime()).thenReturn(runtime);
	}

	private void mockLogger() throws Exception {
		logger = PowerMockito.mock(FileProcessStreamLogger.class);
		PowerMockito.whenNew(FileProcessStreamLogger.class).withNoArguments().thenReturn(logger);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkInvalidCommandError() throws IOException {
		PowerMockito.when(runtime.exec(command)).thenThrow(new IOException());
		checkException(new Task() {

			@Override
			public void perform() {
				executor.executeCommandAndGetOuput();
			}
		}, RuntimeException.class, "Command {" + command + "} could not be executed", IOException.class);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkBadExitValueError() throws Exception {
		PowerMockito.when(runtime.exec(command)).thenReturn(process);
		PowerMockito.when(process.exitValue()).thenReturn(1);
		checkException(new Task() {

			@Override
			public void perform() {
				executor.executeCommandWithTimeout(100);
			}
		}, RuntimeException.class, "Command {" + command + "} returned with error status", null);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldGetCommandOutput() throws IOException {
		PowerMockito.when(runtime.exec(command)).thenReturn(process);

		assertSame(output, executor.executeCommandAndGetOuput());
		Mockito.verify(logger).logErrorStream(process, command);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldLogOutput() throws IOException {
		PowerMockito.when(runtime.exec(command)).thenReturn(process);

		executor.executeCommandWithTimeout(100);
		Mockito.verify(logger).logErrorStream(process, command);
		Mockito.verify(logger).logOutputStream(process, command);
	}
}