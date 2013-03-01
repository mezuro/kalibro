package org.kalibro.core.persistence;

import java.io.IOException;

import org.eclipse.persistence.sessions.Session;
import org.eclipse.persistence.sessions.SessionEvent;
import org.eclipse.persistence.sessions.SessionEventManager;
import org.eclipse.persistence.sessions.UnitOfWork;
import org.junit.Before;
import org.junit.Test;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.tests.UnitTest;
import org.mockito.InOrder;
import org.mockito.Mockito;

public class DatabaseImportTest extends UnitTest {

	private Session session;
	private DatabaseImport databaseImport;

	@Before
	public void setUp() {
		session = mock(Session.class);
		databaseImport = new DatabaseImport();
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
		SessionEvent event = mock(SessionEvent.class);
		UnitOfWork unitOfWork = mock(UnitOfWork.class);
		when(event.getSession()).thenReturn(session);
		when(session.acquireUnitOfWork()).thenReturn(unitOfWork);

		databaseImport.postLogin(event);
		InOrder order = Mockito.inOrder(unitOfWork);
		for (String statement : loadResource("/META-INF/kalibro.sql").split(";"))
			order.verify(unitOfWork).executeNonSelectingSQL(statement);
		order.verify(unitOfWork).commit();
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