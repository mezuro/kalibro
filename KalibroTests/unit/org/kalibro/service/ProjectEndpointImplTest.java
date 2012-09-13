package org.kalibro.service;

import static org.junit.Assert.*;
import static org.kalibro.core.model.ProjectFixtures.helloWorld;
import static org.kalibro.core.model.enums.RepositoryType.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.TestCase;
import org.kalibro.core.model.Project;
import org.kalibro.core.model.enums.RepositoryType;
import org.kalibro.dao.DaoFactory;
import org.kalibro.dao.ProjectDao;
import org.kalibro.service.entities.RawProjectXml;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DaoFactory.class)
public class ProjectEndpointImplTest extends TestCase {

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

	@Test(timeout = UNIT_TIMEOUT)
	public void testSaveProject() {
		endpoint.saveProject(new RawProjectXml(project));
		Mockito.verify(dao).save(project);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testGetProjectNames() {
		List<String> names = new ArrayList<String>();
		when(dao.getProjectNames()).thenReturn(names);
		assertSame(names, endpoint.getProjectNames());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testConfirmProject() {
		when(dao.hasProject("42")).thenReturn(true);
		assertTrue(endpoint.hasProject("42"));

		when(dao.hasProject("42")).thenReturn(false);
		assertFalse(endpoint.hasProject("42"));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testGetProject() {
		when(dao.getProject("42")).thenReturn(project);
		assertDeepEquals(project, endpoint.getProject("42").convert());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testRemoveProject() {
		endpoint.removeProject("42");
		Mockito.verify(dao).removeProject("42");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testSupportedRepositoryTypes() {
		Set<RepositoryType> repositoryTypes = new HashSet<RepositoryType>();
		repositoryTypes.addAll(Arrays.asList(LOCAL_ZIP, GIT, SUBVERSION));
		PowerMockito.when(dao.getSupportedRepositoryTypes()).thenReturn(repositoryTypes);
		assertDeepSet(endpoint.getSupportedRepositoryTypes(), GIT, SUBVERSION);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testProcessProject() {
		endpoint.processProject(PROJECT_NAME);
		PowerMockito.verifyStatic();
		dao.processProject(PROJECT_NAME);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testProcessPeriodically() {
		endpoint.processPeriodically(PROJECT_NAME, 42);
		PowerMockito.verifyStatic();
		dao.processPeriodically(PROJECT_NAME, 42);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testProcessPeriod() {
		PowerMockito.when(dao.getProcessPeriod(PROJECT_NAME)).thenReturn(42);
		assertEquals(42, endpoint.getProcessPeriod(PROJECT_NAME).intValue());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testCancelPeriodicProcess() {
		endpoint.cancelPeriodicProcess(PROJECT_NAME);
		PowerMockito.verifyStatic();
		dao.cancelPeriodicProcess(PROJECT_NAME);
	}
}