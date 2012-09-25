package org.kalibro.client;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Random;
import java.util.Set;

import org.junit.Test;
import org.kalibro.Project;
import org.kalibro.RepositoryType;
import org.kalibro.service.ProjectEndpoint;
import org.kalibro.service.xml.ProjectXmlRequest;
import org.kalibro.service.xml.ProjectXmlResponse;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;

@PrepareOnlyThisForTest(ProjectClientDao.class)
public class ProjectClientDaoTest extends
	ClientTest<Project, ProjectXmlRequest, ProjectXmlResponse, ProjectEndpoint, ProjectClientDao> {

	private static final int PERIOD = new Random().nextInt();
	private static final boolean HAS_PROJECT = new Random().nextBoolean();
	private static final String PROJECT_NAME = "ProjectClientDaoTest project";

	@Override
	protected Class<Project> entityClass() {
		return Project.class;
	}

	@Test
	public void testSave() {
		client.save(entity);
		verify(port).saveProject(request);
	}

	@Test
	public void testGetProjectNames() {
		List<String> names = mock(List.class);
		when(port.getProjectNames()).thenReturn(names);
		assertSame(names, client.getProjectNames());
	}

	@Test
	public void testConfirmProject() {
		when(port.hasProject(PROJECT_NAME)).thenReturn(HAS_PROJECT);
		assertEquals(HAS_PROJECT, client.hasProject(PROJECT_NAME));
	}

	@Test
	public void testGetProject() {
		when(port.getProject(PROJECT_NAME)).thenReturn(response);
		assertSame(entity, client.getProject(PROJECT_NAME));
	}

	@Test
	public void testRemoveProject() {
		client.removeProject(PROJECT_NAME);
		verify(port).removeProject(PROJECT_NAME);
	}

	@Test
	public void testSupportedRepositoryTypes() {
		Set<RepositoryType> repositoryTypes = mock(Set.class);
		when(port.getSupportedRepositoryTypes()).thenReturn(repositoryTypes);
		assertSame(repositoryTypes, client.getSupportedRepositoryTypes());
	}

	@Test
	public void testProcessProject() {
		client.processProject(PROJECT_NAME);
		verify(port).processProject(PROJECT_NAME);
	}

	@Test
	public void testProcessPeriodically() {
		client.processPeriodically(PROJECT_NAME, PERIOD);
		verify(port).processPeriodically(PROJECT_NAME, PERIOD);
	}

	@Test
	public void testProcessPeriod() {
		when(port.getProcessPeriod(PROJECT_NAME)).thenReturn(PERIOD);
		assertEquals(PERIOD, client.getProcessPeriod(PROJECT_NAME).intValue());
	}

	@Test
	public void testCancelPeriodicProcess() {
		client.cancelPeriodicProcess(PROJECT_NAME);
		verify(port).cancelPeriodicProcess(PROJECT_NAME);
	}
}