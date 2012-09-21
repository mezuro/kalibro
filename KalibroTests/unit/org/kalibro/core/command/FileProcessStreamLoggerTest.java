package org.kalibro.core.command;

import static org.kalibro.core.Environment.logsDirectory;
import static org.mockito.Matchers.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.TestCase;
import org.kalibro.core.concurrent.VoidTask;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
@PrepareForTest(FileProcessStreamLogger.class)
public class FileProcessStreamLoggerTest extends TestCase {

	private File logFile;
	private Process process;
	private PipeTask pipeTask;
	private InputStream inputStream;
	private FileOutputStream outputStream;

	private FileProcessStreamLogger logger;

	@Before
	public void setUp() throws Exception {
		logFile = mock(File.class);
		process = mock(Process.class);
		pipeTask = mock(PipeTask.class);
		inputStream = mock(InputStream.class);
		outputStream = mock(FileOutputStream.class);
		logger = new FileProcessStreamLogger();
		when(process.getInputStream()).thenReturn(inputStream);
		when(process.getErrorStream()).thenReturn(inputStream);
		whenNew(File.class).withParameterTypes(File.class, String.class)
			.withArguments(eq(logsDirectory()), anyString()).thenReturn(logFile);
		whenNew(FileOutputStream.class).withArguments(logFile).thenReturn(outputStream);
		whenNew(PipeTask.class).withArguments(inputStream, outputStream).thenReturn(pipeTask);
	}

	@Test
	public void shouldLogStandardOutput() throws Exception {
		logger.logOutputStream(process, "my command");

		verify(process).getInputStream();
		verifyPrivate(logger).invoke("pipe", inputStream, outputStream);
	}

	@Test
	public void shouldLogErrorOutput() throws Exception {
		logger.logErrorStream(process, "my command");

		verify(process).getErrorStream();
		verifyPrivate(logger).invoke("pipe", inputStream, outputStream);
	}

	@Test
	public void shouldWriteCommandOnFirstLine() throws Exception {
		mockStatic(IOUtils.class);
		logger.logOutputStream(process, "my command");

		verifyStatic();
		IOUtils.write("$ my command\n", outputStream);
	}

	@Test
	public void logFileNameShouldContainDate() throws Exception {
		logger.logOutputStream(process, "my command");

		Date date = Whitebox.getInternalState(logger, Date.class);
		String dateString = new SimpleDateFormat("yyyy-MM-dd_HH'h'mm'm'ss.SSS's'").format(date);
		verifyNew(File.class).withArguments(eq(logsDirectory()), eq("my." + dateString + ".out"));
	}

	@Test
	public void checkErrorOnLog() throws Exception {
		whenNew(FileOutputStream.class).withArguments(logFile).thenThrow(new IOException());
		assertThat(new VoidTask() {

			@Override
			protected void perform() {
				logger.logOutputStream(process, "my command");
			}
		}).throwsException().withMessage("Error logging command: my command\nFile: " + logFile)
			.withCause(IOException.class);
	}

	@Test
	public void shouldPipe() {
		logger.logOutputStream(process, "my command");
		verify(pipeTask).executeInBackground();
	}
}