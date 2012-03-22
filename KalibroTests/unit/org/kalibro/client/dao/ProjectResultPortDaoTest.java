package org.kalibro.client.dao;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroTestCase;
import org.kalibro.client.EndpointPortFactory;
import org.kalibro.core.concurrent.Task;
import org.kalibro.core.model.ProjectResult;
import org.kalibro.core.model.ProjectResultFixtures;
import org.kalibro.service.ProjectResultEndpoint;
import org.kalibro.service.entities.ProjectResultXml;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(EndpointPortFactory.class)
public class ProjectResultPortDaoTest extends KalibroTestCase {

	private boolean flag;
	private ProjectResult projectResult;

	private ProjectResultPortDao dao;
	private ProjectResultEndpoint port;

	@Before
	public void setUp() {
		mockPort();
		dao = new ProjectResultPortDao();
		flag = new Random(System.currentTimeMillis()).nextBoolean();
		projectResult = ProjectResultFixtures.helloWorldResult();
	}

	private void mockPort() {
		port = PowerMockito.mock(ProjectResultEndpoint.class);
		PowerMockito.mockStatic(EndpointPortFactory.class);
		PowerMockito.when(EndpointPortFactory.getEndpointPort(ProjectResultEndpoint.class)).thenReturn(port);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotSaveResultRemotely() {
		checkException(new Task() {

			@Override
			public void perform() throws Exception {
				dao.save(projectResult);
			}
		}, UnsupportedOperationException.class, "Cannot save project result remotely");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testHasResultsFor() {
		PowerMockito.when(port.hasResultsFor("")).thenReturn(flag);
		assertEquals(flag, dao.hasResultsFor(""));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testHasResultsBefore() {
		PowerMockito.when(port.hasResultsBefore(null, "")).thenReturn(flag);
		assertEquals(flag, dao.hasResultsBefore(null, ""));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testHasResultsAfter() {
		PowerMockito.when(port.hasResultsAfter(null, "")).thenReturn(flag);
		assertEquals(flag, dao.hasResultsAfter(null, ""));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testGetFirstResultOf() {
		PowerMockito.when(port.getFirstResultOf("")).thenReturn(new ProjectResultXml(projectResult));
		assertDeepEquals(projectResult, dao.getFirstResultOf(""));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testGetLastResultOf() {
		PowerMockito.when(port.getLastResultOf("")).thenReturn(new ProjectResultXml(projectResult));
		assertDeepEquals(projectResult, dao.getLastResultOf(""));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testGetLastResultBefore() {
		PowerMockito.when(port.getLastResultBefore(null, "")).thenReturn(new ProjectResultXml(projectResult));
		assertDeepEquals(projectResult, dao.getLastResultBefore(null, ""));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testGetFirstResultAfter() {
		PowerMockito.when(port.getFirstResultAfter(null, "")).thenReturn(new ProjectResultXml(projectResult));
		assertDeepEquals(projectResult, dao.getFirstResultAfter(null, ""));
	}
}