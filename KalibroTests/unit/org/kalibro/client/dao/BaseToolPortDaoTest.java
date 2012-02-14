package org.kalibro.client.dao;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroTestCase;
import org.kalibro.client.EndpointPortFactory;
import org.kalibro.core.concurrent.Task;
import org.kalibro.core.model.BaseTool;
import org.kalibro.core.model.BaseToolFixtures;
import org.kalibro.service.BaseToolEndpoint;
import org.kalibro.service.entities.BaseToolXml;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(EndpointPortFactory.class)
public class BaseToolPortDaoTest extends KalibroTestCase {

	private BaseTool baseTool;

	private BaseToolPortDao dao;
	private BaseToolEndpoint port;

	@Before
	public void setUp() {
		mockPort();
		dao = new BaseToolPortDao();
		baseTool = BaseToolFixtures.analizoStub();
	}

	private void mockPort() {
		port = PowerMockito.mock(BaseToolEndpoint.class);
		PowerMockito.mockStatic(EndpointPortFactory.class);
		PowerMockito.when(EndpointPortFactory.getEndpointPort(BaseToolEndpoint.class)).thenReturn(port);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testSave() {
		checkException(new Task() {

			@Override
			public void perform() throws Exception {
				dao.save(baseTool);
			}
		}, UnsupportedOperationException.class, "Can not save base tool remotely");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testGetBaseToolNames() {
		List<String> names = new ArrayList<String>();
		PowerMockito.when(port.getBaseToolNames()).thenReturn(names);
		assertSame(names, dao.getBaseToolNames());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testGetBaseTool() {
		PowerMockito.when(port.getBaseTool("")).thenReturn(new BaseToolXml(baseTool));
		assertEquals(baseTool.getName(), dao.getBaseTool("").getName());
		assertEquals(baseTool.getSupportedMetrics(), dao.getBaseTool("").getSupportedMetrics());
	}
}