package org.kalibro.core.persistence;

import static java.util.concurrent.TimeUnit.DAYS;
import static org.junit.Assert.*;
import static org.kalibro.core.model.ProjectFixtures.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

import javax.persistence.TypedQuery;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.Configuration;
import org.kalibro.Project;
import org.kalibro.RepositoryType;
import org.kalibro.core.persistence.record.ProjectRecord;
import org.kalibro.core.processing.ProcessProjectTask;
import org.kalibro.tests.UnitTest;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({FileUtils.class, ProjectDatabaseDao.class, RepositoryType.class})
public class ProjectDatabaseDaoTest extends UnitTest {

	private Project project;
	private RecordManager recordManager;

	private ProjectDatabaseDao dao;

	@Before
	public void setUp() throws Exception {
		project = helloWorld();
		recordManager = mock(RecordManager.class);
		mockConfigurationDao();
		dao = spy(new ProjectDatabaseDao(recordManager));
	}

	private void mockConfigurationDao() throws Exception {
		ConfigurationDatabaseDao configurationDao = mock(ConfigurationDatabaseDao.class);
		whenNew(ConfigurationDatabaseDao.class).withArguments(recordManager).thenReturn(configurationDao);
		when(configurationDao.getConfiguration(anyString())).thenReturn(new Configuration());
	}

	@Test
	public void shouldSave() {
		doReturn(new ArrayList<String>()).when(dao).getAllNames();
		when(recordManager.save(any())).thenReturn(new ProjectRecord(project, null));
		dao.save(project);

		ArgumentCaptor<ProjectRecord> captor = ArgumentCaptor.forClass(ProjectRecord.class);
		Mockito.verify(recordManager).save(captor.capture());
		project.setConfigurationName("");
		assertDeepEquals(project, captor.getValue().convert());
	}

	@Test
	public void shouldListAllProjectNames() {
		doReturn(Arrays.asList("4", "2")).when(dao).getAllNames();
		assertDeepList(dao.getProjectNames(), "4", "2");
	}

	@Test
	public void shouldConfirmProject() {
		doReturn(true).when(dao).hasEntity(PROJECT_NAME);
		assertTrue(dao.hasProject(PROJECT_NAME));

		doReturn(false).when(dao).hasEntity(PROJECT_NAME);
		assertFalse(dao.hasProject(PROJECT_NAME));
	}

	@Test
	public void shouldGetProjectByName() {
		doReturn(project).when(dao).getByName("42");
		assertSame(project, dao.getProject("42"));
	}

	@Test
	public void shouldRemoveProjectByNameAndDependentEntities() {
		TypedQuery<?> query = mockRemoveQueries("42");
		mockRemoveDirectory();
		dao.removeProject("42");

		ArgumentCaptor<ProjectRecord> captor = ArgumentCaptor.forClass(ProjectRecord.class);
		InOrder order = Mockito.inOrder(recordManager, query, recordManager);
		order.verify(recordManager).beginTransaction();
		for (int i = 0; i < 3; i++) {
			order.verify(query).setParameter("projectName", "42");
			order.verify(query).executeUpdate();
		}
		order.verify(recordManager).remove(captor.capture());
		order.verify(recordManager).commitTransaction();

		assertEquals(project.getName(), captor.getValue().convert().getName());
	}

	@Test
	public void shouldRemoveProjectDirectoryOnRemove() {
		mockRemoveQueries("42");
		File directory = mockRemoveDirectory();
		dao.removeProject("42");
		verifyStatic();
		FileUtils.deleteQuietly(directory);
	}

	private TypedQuery<?> mockRemoveQueries(String projectName) {
		doReturn(project).when(dao).getByName(projectName);
		TypedQuery<?> query = mock(TypedQuery.class);
		mockQuery("MetricResult", "module.projectResult.project.name", query);
		mockQuery("Module", "projectResult.project.name", query);
		mockQuery("ProjectResult", "project.name", query);
		return query;
	}

	private void mockQuery(String table, String projectNameField, TypedQuery<?> query) {
		String queryText = "DELETE FROM " + table + " t WHERE t." + projectNameField + " = :projectName";
		doReturn(query).when(dao).createRecordQuery(queryText);
	}

	private File mockRemoveDirectory() {
		File directory = mock(File.class);
		project = spy(project);
		doReturn(project).when(dao).getByName("42");
		doReturn(directory).when(project).getDirectory();
		mockStatic(FileUtils.class);
		return directory;
	}

	@Test
	public void shouldRetrieveSupportedRepositoryTypes() {
		Set<RepositoryType> supportedTypes = mock(Set.class);
		mockStatic(RepositoryType.class);
		when(RepositoryType.supportedTypes()).thenReturn(supportedTypes);
		assertSame(supportedTypes, dao.getSupportedRepositoryTypes());
	}

	@Test
	public void shouldProcessProject() throws Exception {
		ProcessProjectTask task = mockProcessProjectTask(PROJECT_NAME);
		dao.processProject(PROJECT_NAME);
		Mockito.verify(task).executeInBackground();
	}

	@Test
	public void shouldProcessPeriodically() throws Exception {
		ProcessProjectTask task = mockProcessProjectTask(PROJECT_NAME);
		dao.processPeriodically(PROJECT_NAME, 42);
		Mockito.verify(task).executePeriodically(42, DAYS);
	}

	@Test
	public void shouldCancelPreviousPeriodicExecutionIfExistent() throws Exception {
		ProcessProjectTask existent = mockProcessProjectTask(PROJECT_NAME);
		dao.processPeriodically(PROJECT_NAME, 42);

		ProcessProjectTask newTask = mockProcessProjectTask(PROJECT_NAME);
		dao.processPeriodically(PROJECT_NAME, 84);

		Mockito.verify(existent).cancelExecution();
		Mockito.verify(newTask).executePeriodically(84, DAYS);
	}

	@Test
	public void shouldRetrieveProcessPeriod() throws Exception {
		mockProcessProjectTask(PROJECT_NAME);
		dao.processPeriodically(PROJECT_NAME, 42);
		assertEquals(42, dao.getProcessPeriod(PROJECT_NAME).intValue());
	}

	@Test
	public void processPeriodShouldBeZeroIfNotScheduled() throws Exception {
		mockProcessProjectTask(PROJECT_NAME);
		assertEquals(0, dao.getProcessPeriod(PROJECT_NAME).intValue());
	}

	@Test
	public void shouldCancelPeriodicProcess() throws Exception {
		ProcessProjectTask task = mockProcessProjectTask(PROJECT_NAME);
		dao.processPeriodically(PROJECT_NAME, 42);
		dao.cancelPeriodicProcess(PROJECT_NAME);
		Mockito.verify(task).cancelExecution();
		assertEquals(0, dao.getProcessPeriod(PROJECT_NAME).intValue());
	}

	private ProcessProjectTask mockProcessProjectTask(String projectName) throws Exception {
		ProcessProjectTask task = mock(ProcessProjectTask.class);
		whenNew(ProcessProjectTask.class).withArguments(projectName).thenReturn(task);
		return task;
	}
}