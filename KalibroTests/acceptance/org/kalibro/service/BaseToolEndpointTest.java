package org.kalibro.service;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.client.dao.PortDaoFactory;
import org.kalibro.core.concurrent.Task;
import org.kalibro.core.model.BaseTool;
import org.kalibro.core.model.BaseToolFixtures;
import org.kalibro.core.model.NativeMetricFixtures;
import org.kalibro.core.persistence.dao.BaseToolDao;

public class BaseToolEndpointTest extends KalibroServiceTestCase {

	private BaseToolDao dao;

	@Before
	public void setUp() {
		dao = new PortDaoFactory().getBaseToolDao();
	}

	@Test(timeout = ACCEPTANCE_TIMEOUT)
	public void analizoShouldBeInTheSeeds() {
		assertTrue(dao.getBaseToolNames().contains("Analizo"));

		BaseTool analizo = dao.getBaseTool("Analizo");
		assertEquals("Analizo", analizo.getName());
		assertEquals("", analizo.getDescription());
		assertDeepEquals(NativeMetricFixtures.nativeMetrics(), analizo.getSupportedMetrics());
	}

	@Test(timeout = ACCEPTANCE_TIMEOUT)
	public void shouldNotSaveBaseToolRemotely() {
		checkException(new Task() {

			@Override
			public void perform() throws Exception {
				dao.save(BaseToolFixtures.analizoStub());
			}
		}, UnsupportedOperationException.class, "Cannot save base tool remotely");
	}
}