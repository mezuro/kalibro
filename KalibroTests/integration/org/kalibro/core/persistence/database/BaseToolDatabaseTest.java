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

	private BaseTool analizoStub;

	@Before
	public void setUp() {
		analizoStub = analizoStub();
		dao = daoFactory.getBaseToolDao();
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void shouldListSavedBaseToolNames() {
		assertDeepEquals(dao.getBaseToolNames(), "Analizo", "Checkstyle");

		dao.save(analizoStub);
		assertDeepEquals(dao.getBaseToolNames(), "Analizo", "Checkstyle");
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void shouldRetrieveSavedBaseTool() {
		dao.save(analizoStub);
		BaseTool retrieved = dao.getBaseTool(analizoStub.getName());
		assertNotSame(analizoStub, retrieved);
		assertDeepEquals(analizoStub, retrieved);
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void shouldNotRetrieveInexistentBaseTool() {
		checkKalibroException(new Task() {

			@Override
			public void perform() {
				dao.getBaseTool("Inexistent base tool");
			}
		}, "There is no base tool named 'Inexistent base tool'", NoResultException.class);
	}
}