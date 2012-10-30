package org.kalibro.service;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Random;

import org.junit.Test;
import org.kalibro.Project;
import org.kalibro.client.EndpointTest;
import org.kalibro.dao.ProjectDao;
import org.kalibro.service.xml.ProjectXml;
import org.powermock.reflect.Whitebox;

public class ProjectEndpointTest extends EndpointTest<Project, ProjectDao, ProjectEndpoint> {

	private static final Long ID = Math.abs(new Random().nextLong());

	@Override
	protected Project loadFixture() {
		Project project = new Project("ProjectEndpoint test name");
		Whitebox.setInternalState(project, "id", ID);
		return project;
	}

	@Override
	protected List<String> fieldsThatShouldBeProxy() {
		return list("repositories");
	}

	@Test
	public void shouldConfirmExistence() {
		when(dao.exists(ID)).thenReturn(true);
		assertFalse(port.projectExists(-1L));
		assertTrue(port.projectExists(ID));
	}

	@Test
	public void shouldGetById() {
		when(dao.get(ID)).thenReturn(entity);
		assertDeepDtoEquals(entity, port.getProject(ID));
	}

	@Test
	public void shouldGetAll() {
		when(dao.all()).thenReturn(sortedSet(entity));
		assertDeepDtoList(list(entity), port.allProjects());
	}

	@Test
	public void shouldSave() {
		when(dao.save(entity)).thenReturn(ID);
		assertEquals(ID, port.saveProject(new ProjectXml(entity)));
	}

	@Test
	public void shouldDelete() {
		port.deleteProject(ID);
		verify(dao).delete(ID);
	}
}