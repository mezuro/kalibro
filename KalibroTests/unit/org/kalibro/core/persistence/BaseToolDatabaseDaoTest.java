package org.kalibro.core.persistence;

import static org.kalibro.MetricCollectorStub.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.BaseTool;
import org.kalibro.KalibroException;
import org.kalibro.core.Environment;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.tests.ThrowableMatcher;
import org.kalibro.tests.UnitTest;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({BaseToolDatabaseDao.class, IOUtils.class})
public class BaseToolDatabaseDaoTest extends UnitTest {

	private BaseToolDatabaseDao dao;

	@Before
	public void setUp() throws IOException {
		mockStatic(IOUtils.class);
		when(IOUtils.readLines(any(InputStream.class))).thenReturn(list(CLASS_NAME));
		dao = new BaseToolDatabaseDao();
	}

	@Test
	public void shouldGetAllNames() {
		assertDeepEquals(set(NAME), dao.allNames());
	}

	@Test
	public void shouldGetByName() {
		assertDeepEquals(new BaseTool(CLASS_NAME), dao.get(NAME));
	}

	@Test
	public void shouldThowExceptionWhenGettingInvalidBaseTool() {
		assertThat(new VoidTask() {

			@Override
			protected void perform() throws Throwable {
				dao.get("Invalid");
			}
		}).throwsException().withMessage("Base tool not found: Invalid.");
	}

	@Test
	public void shouldThrowExceptionIfCannotReadCollectorClasses() throws IOException {
		IOException error = new IOException();
		when(IOUtils.readLines(any(InputStream.class))).thenThrow(error);
		assertCreateDaoThrowsException().withCause(error).withMessage("Error creating collectors.");
	}

	private ThrowableMatcher assertCreateDaoThrowsException() {
		return assertThat(new VoidTask() {

			@Override
			protected void perform() throws Throwable {
				new BaseToolDatabaseDao();
			}
		}).throwsException();
	}

	@Test
	public void shouldLogErrorCreatingParticularCollector() throws Exception {
		File file = mock(File.class);
		PrintStream printStream = mock(PrintStream.class);
		KalibroException exception = mock(KalibroException.class);

		when(IOUtils.readLines(any(InputStream.class))).thenReturn(list("inexistent.Class"));
		whenNew(File.class).withArguments(Environment.logsDirectory(), "collectors.log").thenReturn(file);
		whenNew(PrintStream.class).withArguments(file).thenReturn(printStream);
		whenNew(KalibroException.class).withArguments(
			eq("Could not load collector of class: inexistent.Class"), any(Exception.class)).thenReturn(exception);

		new BaseToolDatabaseDao();
		verify(exception).printStackTrace(printStream);
	}
}