package org.kalibro.core.command;

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
import org.kalibro.KalibroTestCase;
import org.kalibro.core.util.Directories;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
@PrepareForTest(FileProcessStreamLogger.class)
public class FileProcessStreamLoggerTest extends KalibroTestCase {

	private File logFile;
	private Process process;
	private InputStream inputStream;
	private FileOutputStream outputStream;

	private FileProcessStreamLogger logger;

	@Before
	public void setUp() throws Exception {
		logFile = PowerMockito.mock(File.class);
		process = PowerMockito.mock(Process.class);
		inputStream = PowerMockito.mock(InputStream.class);
		outputStream = PowerMockito.mock(FileOutputStream.class);
		logger = PowerMockito.spy(new FileProcessStreamLogger());
		PowerMockito.when(process.getInputStream()).thenReturn(inputStream);
		PowerMockito.when(process.getErrorStream()).thenReturn(inputStream);
		PowerMockito.whenNew(File.class).withParameterTypes(File.class, String.class)
			.withArguments(eq(Directories.logs()), anyString()).thenReturn(logFile);
		PowerMockito.whenNew(FileOutputStream.class).withArguments(logFile).thenReturn(outputStream);
		PowerMockito.doNothing().when(logger, "pipe", inputStream, outputStream);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldLogStandardOutput() throws Exception {
		logger.logOutputStream(process, "my command");

		Mockito.verify(process).getInputStream();
		PowerMockito.verifyPrivate(logger).invoke("pipe", inputStream, outputStream);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldLogErrorOutput() throws Exception {
		logger.logErrorStream(process, "my command");

		Mockito.verify(process).getErrorStream();
		PowerMockito.verifyPrivate(logger).invoke("pipe", inputStream, outputStream);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldWriteCommandOnFirstLine() throws Exception {
		PowerMockito.mockStatic(IOUtils.class);
		logger.logOutputStream(process, "my command");

		PowerMockito.verifyStatic();
		IOUtils.write("$ my command\n", outputStream);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void logFileNameShouldContainDate() throws Exception {
		logger.logOutputStream(process, "my command");

		Date date = Whitebox.getInternalState(logger, Date.class);
		String dateString = new SimpleDateFormat("yyyy-MM-dd_HH'h'mm'm'ss.SSS's'").format(date);
		PowerMockito.verifyNew(File.class).withArguments(eq(Directories.logs()), eq("my." + dateString + ".out"));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkErrorOnLog() throws Exception {
		PowerMockito.whenNew(FileOutputStream.class).withArguments(logFile).thenThrow(new IOException());
		checkKalibroException(new Runnable() {

			@Override
			public void run() {
				logger.logOutputStream(process, "my command");
			}
		}, "Error logging command: my command\nFile: " + logFile, IOException.class);
	}
}