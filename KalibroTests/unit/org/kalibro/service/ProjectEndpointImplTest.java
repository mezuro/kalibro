package org.kalibro.service;

import static org.junit.Assert.*;
import static org.kalibro.ProjectFixtures.helloWorld;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.Project;
import org.kalibro.dao.DaoFactory;
import org.kalibro.dao.ProjectDao;
import org.kalibro.service.xml.ProjectXmlRequest;
import org.kalibro.tests.UnitTest;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DaoFactory.class)
public class ProjectEndpointImplTest extends UnitTest {

	private static final String PROJECT_NAME = "ProjectEndpointImplTest project";

	private ProjectDao dao;
	private Project project;
	private ProjectEndpointImpl endpoint;

	@Before
	public void setUp() {
		mockDao();
		project = helloWorld();
		endpoint = new ProjectEndpointImpl();
	}

	private void mockDao() {
		dao = mock(ProjectDao.class);
		mockStatic(DaoFactory.class);
		when(DaoFactory.getProjectDao()).thenReturn(dao);
	}

	@Test
	public void testSaveProject() {
		endpoint.saveProject(new ProjectXmlRequest(project));
		Mockito.verify(dao).save(project);
	}

	@Test
	public void testGetProjectNames() {
		List<String> names = new ArrayList<String>();
		when(dao.getProjectNames()).thenReturn(names);
		assertSame(names, endpoint.getProjectNames());
	}

	@Test
	public void testConfirmProject() {
		when(dao.hasProject("42")).thenReturn(true);
		assertTrue(endpoint.hasProject("42"));

		when(dao.hasProject("42")).thenReturn(false);
		assertFalse(endpoint.hasProject("42"));
	}

	@Test
	public void testGetProject() {
		when(dao.getProject("42")).thenReturn(project);
		assertDeepEquals(project, endpoint.getProject("42").convert());
	}

	@Test
	public void testRemoveProject() {
		endpoint.removeProject("42");
		Mockito.verify(dao).removeProject("42");
	}

	@Test
	public void testProcessProject() {
		endpoint.processProject(PROJECT_NAME);
		PowerMockito.verifyStatic();
		dao.processProject(PROJECT_NAME);
	}

	@Test
	public void testProcessPeriodically() {
		endpoint.processPeriodically(PROJECT_NAME, 42);
		PowerMockito.verifyStatic();
		dao.processPeriodically(PROJECT_NAME, 42);
	}

	@Test
	public void testProcessPeriod() {
		PowerMockito.when(dao.getProcessPeriod(PROJECT_NAME)).thenReturn(42);
		assertEquals(42, endpoint.getProcessPeriod(PROJECT_NAME).intValue());
	}

	@Test
	public void testCancelPeriodicProcess() {
		endpoint.cancelPeriodicProcess(PROJECT_NAME);
		PowerMockito.verifyStatic();
		dao.cancelPeriodicProcess(PROJECT_NAME);
	}
}