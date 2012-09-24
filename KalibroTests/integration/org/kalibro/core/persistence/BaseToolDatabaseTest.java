package org.kalibro.core.persistence;

import static org.junit.Assert.assertEquals;

import javax.persistence.NoResultException;

import org.analizo.AnalizoMetricCollector;
import org.checkstyle.CheckstyleMetricCollector;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.kalibro.SupportedDatabase;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.dao.BaseToolDao;
import org.kalibro.dao.DaoFactory;
import org.kalibro.tests.AcceptanceTest;

@RunWith(Parameterized.class)
public class BaseToolDatabaseTest extends AcceptanceTest {

	private BaseToolDao dao;

	public BaseToolDatabaseTest(SupportedDatabase databaseType) {
		super(databaseType);
	}

	@Before
	public void setUp() {
		dao = DaoFactory.getBaseToolDao();
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
			protected void perform() {
				dao.getBaseTool("Inexistent base tool");
			}
		}).throwsException().withMessage("There is no base tool named 'Inexistent base tool'")
			.withCause(NoResultException.class);
	}
}