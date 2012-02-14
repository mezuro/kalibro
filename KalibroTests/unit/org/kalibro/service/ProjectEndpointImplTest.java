package org.kalibro.service;

import static org.junit.Assert.*;
import static org.kalibro.core.model.ProjectFixtures.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.Kalibro;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.model.Project;
import org.kalibro.core.persistence.dao.ProjectDao;
import org.kalibro.service.entities.RawProjectXml;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Kalibro.class)
public class ProjectEndpointImplTest extends KalibroTestCase {

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
		dao = PowerMockito.mock(ProjectDao.class);
		PowerMockito.mockStatic(Kalibro.class);
		PowerMockito.when(Kalibro.getProjectDao()).thenReturn(dao);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testSaveProject() {
		endpoint.saveProject(new RawProjectXml(project));
		Mockito.verify(dao).save(project);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testGetProjectNames() {
		List<String> names = new ArrayList<String>();
		PowerMockito.when(dao.getProjectNames()).thenReturn(names);
		assertSame(names, endpoint.getProjectNames());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testGetProject() {
		PowerMockito.when(dao.getProject("42")).thenReturn(project);
		assertDeepEquals(project, endpoint.getProject("42").convert());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testRemoveProject() {
		endpoint.removeProject("42");
		Mockito.verify(dao).removeProject("42");
	}
}