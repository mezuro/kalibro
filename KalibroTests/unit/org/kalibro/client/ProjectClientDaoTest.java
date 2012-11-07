package org.kalibro.client;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;
import org.kalibro.Project;
import org.kalibro.service.ProjectEndpoint;
import org.kalibro.service.xml.ProjectXml;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;

@PrepareOnlyThisForTest(ProjectClientDao.class)
public class ProjectClientDaoTest extends ClientTest<Project, ProjectXml, ProjectEndpoint, ProjectClientDao> {

	private static final Long ID = Math.abs(new Random().nextLong());

	@Override
	protected Class<Project> entityClass() {
		return Project.class;
	}

	@Test
	public void shouldConfirmExistence() {
		when(port.projectExists(ID)).thenReturn(true);
		assertFalse(client.exists(-1L));
		assertTrue(client.exists(ID));
	}

	@Test
	public void shouldGetById() {
		when(port.getProject(ID)).thenReturn(xml);
		assertSame(entity, client.get(ID));
	}

	@Test
	public void shouldGetProjectOfRepository() {
		when(port.projectOf(ID)).thenReturn(xml);
		assertSame(entity, client.projectOf(ID));
	}

	@Test
	public void shouldGetAll() {
		when(port.allProjects()).thenReturn(list(xml));
		assertDeepEquals(set(entity), client.all());
	}

	@Test
	public void shouldSave() {
		when(port.saveProject(xml)).thenReturn(ID);
		assertEquals(ID, client.save(entity));
	}

	@Test
	public void shouldDelete() {
		client.delete(ID);
		verify(port).deleteProject(ID);
	}
}