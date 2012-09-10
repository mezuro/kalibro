package org.kalibro.client.dao;

import static org.junit.Assert.assertSame;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.TestCase;
import org.kalibro.client.BaseToolPortDao;
import org.kalibro.client.EndpointClient;
import org.kalibro.core.model.BaseTool;
import org.kalibro.service.BaseToolEndpoint;
import org.kalibro.service.entities.BaseToolXml;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
@PrepareForTest({BaseToolPortDao.class, EndpointClient.class})
public class BaseToolPortDaoTest extends TestCase {

	private BaseTool baseTool;
	private BaseToolXml baseToolXml;

	private BaseToolPortDao dao;
	private BaseToolEndpoint port;

	@Before
	public void setUp() throws Exception {
		mockBaseTool();
		createSupressedDao();
	}

	private void mockBaseTool() throws Exception {
		baseTool = mock(BaseTool.class);
		baseToolXml = mock(BaseToolXml.class);
		whenNew(BaseToolXml.class).withArguments(baseTool).thenReturn(baseToolXml);
		when(baseToolXml.convert()).thenReturn(baseTool);
	}

	private void createSupressedDao() {
		suppress(constructor(EndpointClient.class, String.class, Class.class));
		dao = new BaseToolPortDao("");

		port = mock(BaseToolEndpoint.class);
		Whitebox.setInternalState(dao, "port", port);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testGetBaseToolNames() {
		List<String> names = mock(List.class);
		when(port.getBaseToolNames()).thenReturn(names);
		assertSame(names, dao.getBaseToolNames());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testGetBaseTool() {
		when(port.getBaseTool("")).thenReturn(baseToolXml);
		assertSame(baseTool, dao.getBaseTool(""));
	}
}