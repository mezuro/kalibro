package org.kalibro.client;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.TestCase;
import org.kalibro.core.concurrent.Task;
import org.kalibro.core.model.ProjectResult;
import org.kalibro.service.ProjectResultEndpoint;
import org.kalibro.service.entities.ProjectResultXml;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ProjectResultClientDao.class, EndpointClient.class})
public class ProjectResultPortDaoTest extends TestCase {

	private boolean flag;
	private ProjectResult projectResult;
	private ProjectResultXml projectResultXml;

	private ProjectResultClientDao dao;
	private ProjectResultEndpoint port;

	@Before
	public void setUp() throws Exception {
		flag = new Random(System.currentTimeMillis()).nextBoolean();
		mockProjectResult();
		createSupressedDao();
	}

	private void mockProjectResult() throws Exception {
		projectResult = mock(ProjectResult.class);
		projectResultXml = mock(ProjectResultXml.class);
		whenNew(ProjectResultXml.class).withArguments(projectResult).thenReturn(projectResultXml);
		when(projectResultXml.convert()).thenReturn(projectResult);
	}

	private void createSupressedDao() {
		suppress(constructor(EndpointClient.class, String.class, Class.class));
		dao = new ProjectResultClientDao("");

		port = mock(ProjectResultEndpoint.class);
		Whitebox.setInternalState(dao, "port", port);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotSaveResultRemotely() {
		checkKalibroException(new Task() {

			@Override
			public void perform() {
				dao.save(projectResult);
			}
		}, "Cannot save project result remotely");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testHasResultsFor() {
		when(port.hasResultsFor("")).thenReturn(flag);
		assertEquals(flag, dao.hasResultsFor(""));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testHasResultsBefore() {
		when(port.hasResultsBefore(null, "")).thenReturn(flag);
		assertEquals(flag, dao.hasResultsBefore(null, ""));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testHasResultsAfter() {
		when(port.hasResultsAfter(null, "")).thenReturn(flag);
		assertEquals(flag, dao.hasResultsAfter(null, ""));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testGetFirstResultOf() {
		when(port.getFirstResultOf("")).thenReturn(projectResultXml);
		assertSame(projectResult, dao.getFirstResultOf(""));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testGetLastResultOf() {
		PowerMockito.when(port.getLastResultOf("")).thenReturn(projectResultXml);
		assertSame(projectResult, dao.getLastResultOf(""));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testGetLastResultBefore() {
		PowerMockito.when(port.getLastResultBefore(null, "")).thenReturn(projectResultXml);
		assertSame(projectResult, dao.getLastResultBefore(null, ""));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testGetFirstResultAfter() {
		PowerMockito.when(port.getFirstResultAfter(null, "")).thenReturn(projectResultXml);
		assertSame(projectResult, dao.getFirstResultAfter(null, ""));
	}
}