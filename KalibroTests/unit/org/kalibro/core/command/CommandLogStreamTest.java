package org.kalibro.core.command;

import static org.kalibro.core.Environment.logsDirectory;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.tests.UnitTest;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({CommandLogStream.class, IOUtils.class})
public class CommandLogStreamTest extends UnitTest {

	private File logFile;
	private FileOutputStream fileStream;

	private CommandLogStream logStream;

	@Before
	public void setUp() throws Exception {
		logFile = mock(File.class);
		fileStream = mock(FileOutputStream.class);
		logStream = new CommandLogStream(
				"analizo metrics --list", "out");
		whenNew(File.class).withParameterTypes(File.class, String.class)
			.withArguments(eq(logsDirectory()),
					anyString()).thenReturn(logFile);
		whenNew(FileOutputStream.class).withArguments(logFile, true)
			.thenReturn(fileStream);
		mockStatic(IOUtils.class);
	}

	@Test
	public void shouldCreateFileStreamOnlyOnFirstWrite() {
		verifyZeroInteractions(logFile, fileStream, IOUtils.class);
	}

	@Test
	public void shouldWriteBytesOnFileStream() throws Exception {
		logStream.write(42);
		verify(fileStream).write(42);
	}

	@Test
	public void shouldWriteCommandOnStream() throws Exception {
		logStream.write(42);
		IOUtils.write("\n\n$ analizo metrics --list\n", fileStream);
	}

	@Test
	public void shouldWriteOnFileWithDateOnName() throws Exception {
		logStream.write(42);
		String today = new SimpleDateFormat("yyyy-MM-dd")
			.format(new Date());
		verifyNew(File.class).withArguments(eq(logsDirectory()),
				eq("analizo." + today + ".out"));
	}
}
