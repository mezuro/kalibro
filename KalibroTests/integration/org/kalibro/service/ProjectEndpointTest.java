package org.kalibro.service;

import static org.kalibro.ProjectFixtures.newHelloWorld;

import org.junit.Test;
import org.kalibro.KalibroException;
import org.kalibro.Project;
import org.kalibro.client.EndpointTest;
import org.kalibro.dao.ProjectDao;
import org.kalibro.service.xml.ProjectXmlRequest;

public class ProjectEndpointTest extends EndpointTest<Project, ProjectDao, ProjectEndpoint> {

	@Override
	protected Project loadFixture() {
		Project fixture = newHelloWorld();
		fixture.setError(new KalibroException("ProjectEndpointTest exception", new Exception()));
		return fixture;
	}

	@Test
	public void shouldListProjectNames() {
		when(dao.getProjectNames()).thenReturn(asList("42"));
		assertDeepEquals(asList("42"), port.getProjectNames());
	}

	@Test
	public void shouldGetProjectByName() {
		when(dao.getProject("42")).thenReturn(entity);
		assertDeepDtoEquals(entity, port.getProject("42"));
	}

	@Test
	public void shouldRemoveProjectByName() {
		port.removeProject("42");
		verify(dao).removeProject("42");
	}

	@Test
	public void shouldSaveProject() {
		port.saveProject(new ProjectXmlRequest(entity));
		verify(dao).save(entity);
	}
}