package org.kalibro.service;

import static org.junit.Assert.assertTrue;
import static org.kalibro.core.model.ProjectFixtures.newHelloWorld;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroException;
import org.kalibro.core.model.Project;
import org.kalibro.service.entities.RawProjectXml;

public class ProjectEndpointTest extends OldEndpointTest {

	private Project sample;
	private ProjectEndpoint port;

	@Before
	public void setUp() {
		sample = newHelloWorld();
		sample.setError(new KalibroException("ProjectEndpointTest", new Exception()));
		ProjectDaoFake daoFake = new ProjectDaoFake();
		daoFake.save(sample);
		port = publishAndGetPort(new ProjectEndpointImpl(daoFake), ProjectEndpoint.class);
	}

	@Test
	public void shouldListProjectNames() {
		assertDeepList(port.getProjectNames(), sample.getName());
	}

	@Test
	public void shouldGetProjectByName() {
		assertDeepEquals(sample, port.getProject(sample.getName()).convert());
	}

	@Test
	public void shouldRemoveProjectByName() {
		port.removeProject(sample.getName());
		assertTrue(port.getProjectNames().isEmpty());
	}

	@Test
	public void shouldSaveProject() {
		Project newProject = newHelloWorld();
		newProject.setName("ProjectEndpointTest project");
		port.saveProject(new RawProjectXml(newProject));
		assertDeepList(port.getProjectNames(), sample.getName(), newProject.getName());
	}
}