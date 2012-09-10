package org.kalibro.client.dao;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.TestCase;
import org.kalibro.client.EndpointClient;
import org.kalibro.client.ProjectPortDao;
import org.kalibro.core.model.Project;
import org.kalibro.service.ProjectEndpoint;
import org.kalibro.service.entities.ProjectXml;
import org.kalibro.service.entities.RawProjectXml;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ProjectPortDao.class, EndpointClient.class})
public class ProjectPortDaoTest extends TestCase {

	private Project project;
	private ProjectXml projectXml;
	private RawProjectXml rawProjectXml;

	private ProjectPortDao dao;
	private ProjectEndpoint port;

	@Before
	public void setUp() throws Exception {
		mockProject();
		createSupressedDao();
	}

	private void mockProject() throws Exception {
		project = mock(Project.class);
		projectXml = mock(ProjectXml.class);
		rawProjectXml = mock(RawProjectXml.class);
		when(projectXml.convert()).thenReturn(project);
		whenNew(RawProjectXml.class).withArguments(project).thenReturn(rawProjectXml);
	}

	private void createSupressedDao() {
		suppress(constructor(EndpointClient.class, String.class, Class.class));
		dao = new ProjectPortDao("");

		port = mock(ProjectEndpoint.class);
		Whitebox.setInternalState(dao, "port", port);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testSave() {
		dao.save(project);
		verify(port).saveProject(rawProjectXml);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testGetProjectNames() {
		List<String> names = mock(List.class);
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
		when(port.getProject("")).thenReturn(projectXml);
		assertSame(project, dao.getProject(""));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testRemoveProject() {
		dao.removeProject("");
		verify(port).removeProject("");
	}
}
