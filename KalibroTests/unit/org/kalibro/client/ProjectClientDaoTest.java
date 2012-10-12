package org.kalibro.client;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;
import org.kalibro.Project;
import org.kalibro.service.ProjectEndpoint;
import org.kalibro.service.xml.ProjectXmlRequest;
import org.kalibro.service.xml.ProjectXmlResponse;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;

@PrepareOnlyThisForTest(ProjectClientDao.class)
public class ProjectClientDaoTest extends ClientTest<// @formatter:off
	Project, ProjectXmlRequest, ProjectXmlResponse,	ProjectEndpoint, ProjectClientDao> {// @formatter:on

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
		when(port.getProject(ID)).thenReturn(response);
		assertSame(entity, client.get(ID));
	}

	@Test
	public void shouldGetAll() {
		when(port.allProjects()).thenReturn(asList(response));
		assertDeepEquals(asSet(entity), client.all());
	}

	@Test
	public void shouldSave() {
		when(port.saveProject(request)).thenReturn(ID);
		assertEquals(ID, client.save(entity));
	}

	@Test
	public void shouldDelete() {
		client.delete(ID);
		verify(port).deleteProject(ID);
	}
}