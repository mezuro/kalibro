package org.kalibro.core.persistence;

import static org.junit.Assert.*;
import static org.kalibro.core.Environment.logsDirectory;

import java.io.File;
import java.io.FileWriter;

import org.eclipse.persistence.logging.DefaultSessionLog;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.tests.UnitTest;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({DefaultSessionLog.class, PersistenceLogger.class})
public class PersistenceLoggerTest extends UnitTest {

	@Test
	public void shouldLogAsDefaultLogger() {
		assertEquals(DefaultSessionLog.class, PersistenceLogger.class.getSuperclass());
	}

	@Test
	public void shouldWriteOnLogsDirectory() throws Exception {
		File logFile = new File(logsDirectory(), "JPA.log");
		FileWriter writer = mock(FileWriter.class);
		whenNew(FileWriter.class).withArguments(logFile).thenReturn(writer);

		assertSame(writer, new PersistenceLogger().getWriter());
	}
}