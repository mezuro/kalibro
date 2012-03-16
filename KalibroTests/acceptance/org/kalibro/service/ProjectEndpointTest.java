package org.kalibro.service;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.client.dao.PortDaoFactory;
import org.kalibro.core.model.Project;
import org.kalibro.core.model.ProjectFixtures;
import org.kalibro.core.model.enums.ProjectState;
import org.kalibro.core.persistence.dao.ProjectDao;

public class ProjectEndpointTest extends KalibroServiceTestCase {

	private ProjectDao dao;

	@Before
	public void setUp() {
		dao = new PortDaoFactory().getProjectDao();
	}

	@Test(timeout = ACCEPTANCE_TIMEOUT)
	public void shouldSaveRetrieveAndRemoveProject() {
		String projectName = "ProjectEndpointTest project";
		Project project = ProjectFixtures.helloWorld();
		project.setName(projectName);
		project.setState(ProjectState.NEW);

		assertFalse(dao.getProjectNames().contains(projectName));

		dao.save(project);
		assertTrue(dao.getProjectNames().contains(projectName));
		assertDeepEquals(project, dao.getProject(projectName));

		dao.removeProject(projectName);
		assertFalse(dao.getProjectNames().contains(projectName));
	}
}