package org.kalibro.service;

import static org.junit.Assert.*;
import static org.kalibro.core.model.ProjectFixtures.helloWorld;
import static org.powermock.api.mockito.PowerMockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.TestCase;
import org.kalibro.core.dao.DaoFactory;
import org.kalibro.core.dao.ProjectDao;
import org.kalibro.core.model.Project;
import org.kalibro.service.entities.RawProjectXml;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DaoFactory.class)
public class ProjectEndpointImplTest extends TestCase {

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
}