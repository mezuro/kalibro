package org.kalibro.core.persistence.database;

import static org.junit.Assert.*;
import static org.kalibro.core.model.BaseToolFixtures.*;

import javax.persistence.NoResultException;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.core.concurrent.Task;
import org.kalibro.core.model.BaseTool;

public abstract class BaseToolDatabaseTest extends DatabaseTestCase {

	private BaseToolDatabaseDao dao;

	private BaseTool analizo;

	@Before
	public void setUp() {
		analizo = analizoStub();
		dao = daoFactory.getBaseToolDao();
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void shouldListSavedBaseToolNames() {
		assertDeepEquals(dao.getBaseToolNames(), "Analizo");

		dao.save(analizo);
		assertDeepEquals(dao.getBaseToolNames(), "Analizo");
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void shouldRetrieveSavedBaseTool() {
		dao.save(analizo);
		BaseTool retrieved = dao.getBaseTool(analizo.getName());
		assertNotSame(analizo, retrieved);
		assertDeepEquals(analizo, retrieved);
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void shouldNotRetrieveRemovedBaseTool() {
		checkException(new Task() {

			@Override
			public void perform() {
				dao.getBaseTool("Inexistent base tool");
			}
		}, NoResultException.class, "There is no base tool named 'Inexistent base tool'");
	}
}