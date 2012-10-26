package org.kalibro.service;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;
import org.kalibro.Project;
import org.kalibro.dao.ProjectDao;
import org.kalibro.service.xml.ProjectXml;
import org.powermock.core.classloader.annotations.PrepareForTest;

@PrepareForTest(ProjectEndpointImpl.class)
public class ProjectEndpointImplTest extends
	EndpointImplementorTest<Project, ProjectXml, ProjectXml, ProjectDao, ProjectEndpointImpl> {

	private static final Long ID = Math.abs(new Random().nextLong());

	@Override
	protected Class<Project> entityClass() {
		return Project.class;
	}

	@Test
	public void shouldConfirmExistence() {
		when(dao.exists(ID)).thenReturn(true);
		assertFalse(implementor.projectExists(-1L));
		assertTrue(implementor.projectExists(ID));
	}

	@Test
	public void shouldGetById() {
		when(dao.get(ID)).thenReturn(entity);
		assertSame(response, implementor.getProject(ID));
	}

	@Test
	public void shouldGetAll() {
		when(dao.all()).thenReturn(sortedSet(entity));
		assertDeepEquals(list(response), implementor.allProjects());
	}

	@Test
	public void shouldSave() {
		when(dao.save(entity)).thenReturn(ID);
		assertEquals(ID, implementor.saveProject(request));
	}

	@Test
	public void shouldDelete() {
		implementor.deleteProject(ID);
		verify(dao).delete(ID);
	}
}