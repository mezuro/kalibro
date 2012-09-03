package org.kalibro.core.persistence;

import static org.junit.Assert.assertEquals;

import javax.persistence.NoResultException;

import org.analizo.AnalizoMetricCollector;
import org.checkstyle.CheckstyleMetricCollector;
import org.junit.Before;
import org.junit.Test;
import org.kalibro.core.concurrent.Task;

public abstract class BaseToolDatabaseTest extends DatabaseTestCase {

	private BaseToolDatabaseDao dao;

	@Before
	public void setUp() {
		dao = daoFactory.createBaseToolDao();
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void shouldListSavedBaseToolNames() {
		assertDeepList(dao.getBaseToolNames(), "Analizo", "Checkstyle", "CVSAnaly");
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void shouldRetrieveSavedBaseTool() {
		assertEquals(AnalizoMetricCollector.class, dao.getBaseTool("Analizo").getCollectorClass());
		assertEquals(CheckstyleMetricCollector.class, dao.getBaseTool("Checkstyle").getCollectorClass());
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