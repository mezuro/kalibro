package org.kalibro.service;

import static org.junit.Assert.assertSame;
import static org.kalibro.core.model.BaseToolFixtures.analizoStub;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.TestCase;
import org.kalibro.core.model.BaseTool;
import org.kalibro.core.persistence.dao.BaseToolDao;
import org.kalibro.core.persistence.dao.DaoFactory;
import org.kalibro.service.entities.BaseToolXmlTest;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DaoFactory.class)
public class BaseToolEndpointImplTest extends TestCase {

	private BaseToolDao dao;
	private BaseTool baseTool;
	private BaseToolEndpointImpl endpoint;

	@Before
	public void setUp() {
		mockDao();
		baseTool = analizoStub();
		endpoint = new BaseToolEndpointImpl();
	}

	private void mockDao() {
		dao = PowerMockito.mock(BaseToolDao.class);
		PowerMockito.mockStatic(DaoFactory.class);
		PowerMockito.when(DaoFactory.getBaseToolDao()).thenReturn(dao);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testGetBaseToolNames() {
		List<String> names = new ArrayList<String>();
		PowerMockito.when(dao.getBaseToolNames()).thenReturn(names);
		assertSame(names, endpoint.getBaseToolNames());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testGetBaseTool() {
		PowerMockito.when(dao.getBaseTool("42")).thenReturn(baseTool);
		new BaseToolXmlTest().assertCorrectConversion(baseTool, endpoint.getBaseTool("42").convert());
	}
}