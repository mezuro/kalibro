package org.kalibro.core.persistence;

import static org.junit.Assert.assertEquals;

import javax.persistence.NoResultException;

import org.analizo.AnalizoMetricCollector;
import org.checkstyle.CheckstyleMetricCollector;
import org.junit.Before;
import org.junit.Test;
import org.kalibro.core.concurrent.VoidTask;

public abstract class BaseToolDatabaseTest extends DatabaseTestCase {

	private BaseToolDatabaseDao dao;

	@Before
	public void setUp() {
		dao = daoFactory.createBaseToolDao();
	}

	@Test
	public void shouldListSavedBaseToolNames() {
		assertDeepList(dao.getBaseToolNames(), "Analizo", "Checkstyle", "CVSAnaly");
	}

	@Test
	public void shouldRetrieveSavedBaseTool() {
		assertEquals(AnalizoMetricCollector.class, dao.getBaseTool("Analizo").getCollectorClass());
		assertEquals(CheckstyleMetricCollector.class, dao.getBaseTool("Checkstyle").getCollectorClass());
	}

	@Test
	public void shouldNotRetrieveInexistentBaseTool() {
		assertThat(new VoidTask() {

			@Override
			public void perform() {
				dao.getBaseTool("Inexistent base tool");
			}
		}).throwsException().withMessage("There is no base tool named 'Inexistent base tool'")
			.withCause(NoResultException.class);
	}
}