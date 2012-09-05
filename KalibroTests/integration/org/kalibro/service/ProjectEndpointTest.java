package org.kalibro.service;

import static org.junit.Assert.assertTrue;
import static org.kalibro.core.model.ProjectFixtures.newHelloWorld;

import java.net.MalformedURLException;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroException;
import org.kalibro.core.dao.ProjectDaoFake;
import org.kalibro.core.model.Project;
import org.kalibro.service.entities.RawProjectXml;

public class ProjectEndpointTest extends KalibroServiceTestCase {

	private Project sample;
	private ProjectEndpoint port;

	@Before
	public void setUp() throws MalformedURLException {
		sample = newHelloWorld();
		sample.setError(new KalibroException("ProjectEndpointTest", new Exception()));
		ProjectDaoFake daoFake = new ProjectDaoFake();
		daoFake.save(sample);
		port = publishAndGetPort(new ProjectEndpointImpl(daoFake), ProjectEndpoint.class);
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void shouldListProjectNames() {
		assertDeepList(port.getProjectNames(), sample.getName());
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void shouldGetProjectByName() {
		assertDeepEquals(sample, port.getProject(sample.getName()).convert());
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void shouldRemoveProjectByName() {
		port.removeProject(sample.getName());
		assertTrue(port.getProjectNames().isEmpty());
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void shouldSaveProject() {
		Project newProject = newHelloWorld();
		newProject.setName("ProjectEndpointTest project");
		port.saveProject(new RawProjectXml(newProject));
		assertDeepList(port.getProjectNames(), sample.getName(), newProject.getName());
	}
}