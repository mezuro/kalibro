package org.kalibro.client.dao;

import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.TestCase;
import org.kalibro.client.EndpointPortFactory;
import org.kalibro.core.model.Project;
import org.kalibro.core.model.ProjectFixtures;
import org.kalibro.service.ProjectEndpoint;
import org.kalibro.service.entities.ProjectXml;
import org.kalibro.service.entities.RawProjectXml;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(EndpointPortFactory.class)
public class ProjectPortDaoTest extends TestCase {

	private Project project;

	private ProjectPortDao dao;
	private ProjectEndpoint port;

	@Before
	public void setUp() {
		mockPort();
		dao = new ProjectPortDao();
		project = ProjectFixtures.helloWorld();
	}

	private void mockPort() {
		port = PowerMockito.mock(ProjectEndpoint.class);
		mockStatic(EndpointPortFactory.class);
		when(EndpointPortFactory.getEndpointPort(ProjectEndpoint.class)).thenReturn(port);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testSave() {
		dao.save(project);

		ArgumentCaptor<RawProjectXml> captor = ArgumentCaptor.forClass(RawProjectXml.class);
		Mockito.verify(port).saveProject(captor.capture());
		assertEquals(project, captor.getValue().convert());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testGetProjectNames() {
		List<String> names = new ArrayList<String>();
		when(port.getProjectNames()).thenReturn(names);
		assertSame(names, dao.getProjectNames());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testConfirmProject() {
		when(port.hasProject("42")).thenReturn(true);
		assertTrue(dao.hasProject("42"));

		when(port.hasProject("42")).thenReturn(false);
		assertFalse(dao.hasProject("42"));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testGetProject() {
		when(port.getProject("")).thenReturn(new ProjectXml(project));
		assertDeepEquals(project, dao.getProject(""));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testRemoveProject() {
		dao.removeProject("");
		Mockito.verify(port).removeProject("");
	}
}