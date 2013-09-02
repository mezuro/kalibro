package org.kalibro.core.persistence;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.persistence.sessions.Session;
import org.eclipse.persistence.sessions.SessionEvent;
import org.eclipse.persistence.sessions.SessionEventManager;
import org.eclipse.persistence.sessions.UnitOfWork;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroSettings;
import org.kalibro.core.Environment;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.tests.UnitTest;
import org.mockito.InOrder;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Environment.class, KalibroSettings.class})
public class DatabaseImportTest extends UnitTest {

	private Session session;
	private SessionEvent event;
	private UnitOfWork unitOfWork;

	private DatabaseImport databaseImport;

	@Before
	public void setUp() {
		session = mock(Session.class);
		mockUnitOfWork();
		mockSettings();
		databaseImport = new DatabaseImport();
	}

	private void mockUnitOfWork() {
		event = mock(SessionEvent.class);
		unitOfWork = mock(UnitOfWork.class);
		when(event.getSession()).thenReturn(session);
		when(session.acquireUnitOfWork()).thenReturn(unitOfWork);
	}

	private void mockSettings() {
		mockStatic(KalibroSettings.class);
		when(KalibroSettings.load()).thenReturn(new KalibroSettings());
	}

	@Test
	public void shouldCustomizeSession() throws Exception {
		SessionEventManager eventManager = mock(SessionEventManager.class);
		when(session.getEventManager()).thenReturn(eventManager);

		databaseImport.customize(session);
		verify(eventManager).addListener(databaseImport);
	}

	@Test
	public void shouldImportDatabase() throws IOException {
		databaseImport.postLogin(event);
		InOrder order = Mockito.inOrder(unitOfWork);
		List<String> statements = new ArrayList<String>();
		statements.addAll(Arrays.asList(loadResource("/META-INF/PostgreSQL/clean.sql").split("\n\n")));
		statements.addAll(Arrays.asList(loadResource("/META-INF/PostgreSQL/create.sql").split("\n\n")));
		statements.addAll(Arrays.asList(loadResource("/META-INF/PostgreSQL/functions.sql").split("\n\n")));
		for (String statement : statements)
			order.verify(unitOfWork).executeNonSelectingSQL(statement);
		order.verify(unitOfWork).commit();
	}

	@Test
	public void shouldNotDropTablesIfNotTesting() {
		mockStatic(Environment.class);
		when(Environment.testing()).thenReturn(false);

		databaseImport.postLogin(event);
		verify(unitOfWork, never()).executeNonSelectingSQL(Matchers.contains("DROP TABLE"));
		verify(unitOfWork).commit();
	}

	@Test
	public void shouldThrowKalibroExceptionOnError() {
		assertThat(new VoidTask() {

			@Override
			protected void perform() throws Throwable {
				databaseImport.postLogin(null);
			}
		}).throwsException().withMessage("Could not import database.").withCause(NullPointerException.class);
	}
}