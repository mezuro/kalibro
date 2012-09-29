package org.kalibro.core.persistence;

import static org.junit.Assert.assertEquals;

import javax.persistence.NoResultException;

import org.analizo.AnalizoMetricCollector;
import org.checkstyle.CheckstyleMetricCollector;
import org.junit.Before;
import org.junit.Test;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.dao.BaseToolDao;
import org.kalibro.dao.DaoFactory;
import org.kalibro.tests.AcceptanceTest;

public class BaseToolDatabaseTest extends AcceptanceTest {

	private BaseToolDao dao;

	@Before
	public void setUp() {
		dao = DaoFactory.getBaseToolDao();
	}

	@Test
	public void shouldListSavedBaseToolNames() {
		assertDeepEquals(asList("Analizo", "Checkstyle", "CVSAnaly"), dao.getBaseToolNames());
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
			protected void perform() {
				dao.getBaseTool("Inexistent base tool");
			}
		}).throwsException().withMessage("There is no base tool named 'Inexistent base tool'")
			.withCause(NoResultException.class);
	}
}