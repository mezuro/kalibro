package org.kalibro.client;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.Project;
import org.kalibro.RepositoryType;
import org.kalibro.service.ProjectEndpoint;
import org.kalibro.service.xml.ProjectXml;
import org.kalibro.service.xml.RawProjectXml;
import org.kalibro.tests.UnitTest;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ProjectClientDao.class, EndpointClient.class})
public class ProjectPortDaoTest extends UnitTest {

	private static final String PROJECT_NAME = "ProjectPortDaoTest project";

	private Project project;
	private ProjectXml projectXml;
	private RawProjectXml rawProjectXml;

	private ProjectClientDao dao;
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
		dao = new ProjectClientDao("");

		port = mock(ProjectEndpoint.class);
		Whitebox.setInternalState(dao, "port", port);
	}

	@Test
	public void testSave() {
		dao.save(project);
		verify(port).saveProject(rawProjectXml);
	}

	@Test
	public void testGetProjectNames() {
		List<String> names = mock(List.class);
		when(port.getProjectNames()).thenReturn(names);
		assertSame(names, dao.getProjectNames());
	}

	@Test
	public void testConfirmProject() {
		when(port.hasProject("42")).thenReturn(true);
		assertTrue(dao.hasProject("42"));

		when(port.hasProject("42")).thenReturn(false);
		assertFalse(dao.hasProject("42"));
	}

	@Test
	public void testGetProject() {
		when(port.getProject("")).thenReturn(projectXml);
		assertSame(project, dao.getProject(""));
	}

	@Test
	public void testRemoveProject() {
		dao.removeProject("");
		verify(port).removeProject("");
	}

	@Test
	public void testSupportedRepositoryTypes() {
		Set<RepositoryType> repositoryTypes = new HashSet<RepositoryType>();
		when(port.getSupportedRepositoryTypes()).thenReturn(repositoryTypes);
		assertSame(repositoryTypes, dao.getSupportedRepositoryTypes());
	}

	@Test
	public void testProcessProject() {
		dao.processProject(PROJECT_NAME);
		verify(port).processProject(PROJECT_NAME);
	}

	@Test
	public void testProcessPeriodically() {
		dao.processPeriodically(PROJECT_NAME, 42);
		verify(port).processPeriodically(PROJECT_NAME, 42);
	}

	@Test
	public void testProcessPeriod() {
		when(port.getProcessPeriod(PROJECT_NAME)).thenReturn(42);
		assertEquals(42, dao.getProcessPeriod(PROJECT_NAME).intValue());
	}

	@Test
	public void testCancelPeriodicProcess() {
		dao.cancelPeriodicProcess(PROJECT_NAME);
		verify(port).cancelPeriodicProcess(PROJECT_NAME);
	}
}