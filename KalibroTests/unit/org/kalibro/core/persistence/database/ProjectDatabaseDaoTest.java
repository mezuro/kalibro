package org.kalibro.core.persistence.database;

import static org.junit.Assert.*;
import static org.kalibro.core.model.ProjectFixtures.*;
import static org.mockito.Matchers.*;
import static org.powermock.api.mockito.PowerMockito.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.Kalibro;
import org.kalibro.core.model.Configuration;
import org.kalibro.core.model.Project;
import org.kalibro.core.persistence.database.entities.ProjectRecord;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({FileUtils.class, Kalibro.class, ProjectDatabaseDao.class})
public class ProjectDatabaseDaoTest extends KalibroTestCase {

	private Project project;
	private DatabaseManager databaseManager;

	private ProjectDatabaseDao dao;

	@Before
	public void setUp() throws Exception {
		project = helloWorld();
		databaseManager = mock(DatabaseManager.class);
		mockConfigurationDao();
		dao = spy(new ProjectDatabaseDao(databaseManager));
	}

	private void mockConfigurationDao() throws Exception {
		ConfigurationDatabaseDao configurationDao = mock(ConfigurationDatabaseDao.class);
		whenNew(ConfigurationDatabaseDao.class).withArguments(databaseManager).thenReturn(configurationDao);
		when(configurationDao.getConfiguration(anyString())).thenReturn(new Configuration());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSave() {
		doReturn(new ArrayList<String>()).when(dao).getAllNames();
		when(databaseManager.save(any())).thenReturn(new ProjectRecord(project, null));
		dao.save(project);

		ArgumentCaptor<ProjectRecord> captor = ArgumentCaptor.forClass(ProjectRecord.class);
		Mockito.verify(databaseManager).save(captor.capture());
		project.setConfigurationName("");
		assertDeepEquals(project, captor.getValue().convert());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldListAllProjectNames() {
		doReturn(Arrays.asList("4", "2")).when(dao).getAllNames();
		assertDeepList(dao.getProjectNames(), "4", "2");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldConfirmProject() {
		doReturn(true).when(dao).hasEntity(PROJECT_NAME);
		assertTrue(dao.hasProject(PROJECT_NAME));

		doReturn(false).when(dao).hasEntity(PROJECT_NAME);
		assertFalse(dao.hasProject(PROJECT_NAME));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldGetProjectByName() {
		doReturn(project).when(dao).getByName("42");
		assertSame(project, dao.getProject("42"));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRemoveProjectByNameAndDependentEntities() {
		Query<?> query = mockRemoveQueries("42");
		mockRemoveDirectory();
		dao.removeProject("42");

		ArgumentCaptor<ProjectRecord> captor = ArgumentCaptor.forClass(ProjectRecord.class);
		InOrder order = Mockito.inOrder(databaseManager, query, databaseManager);
		order.verify(databaseManager).beginTransaction();
		for (int i = 0; i < 3; i++) {
			order.verify(query).setParameter("projectName", "42");
			order.verify(query).executeUpdate();
		}
		order.verify(databaseManager).remove(captor.capture());
		order.verify(databaseManager).commitTransaction();

		assertEquals(project.getName(), captor.getValue().convert().getName());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRemoveProjectDirectoryOnRemove() {
		mockRemoveQueries("42");
		File directory = mockRemoveDirectory();
		dao.removeProject("42");
		verifyStatic();
		FileUtils.deleteQuietly(directory);
	}

	private Query<?> mockRemoveQueries(String projectName) {
		doReturn(project).when(dao).getByName(projectName);
		Query<?> query = mock(Query.class);
		mockQuery("MetricResult", "module.projectResult.project.name", query);
		mockQuery("Module", "projectResult.project.name", query);
		mockQuery("ProjectResult", "project.name", query);
		return query;
	}

	private void mockQuery(String table, String projectNameField, Query<?> query) {
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
}