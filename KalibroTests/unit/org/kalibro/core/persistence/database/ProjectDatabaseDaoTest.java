package org.kalibro.core.persistence.database;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.model.Project;
import org.kalibro.core.model.ProjectFixtures;
import org.kalibro.core.persistence.database.entities.ProjectRecord;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

public class ProjectDatabaseDaoTest extends KalibroTestCase {

	private Project project;
	private DatabaseManager databaseManager;

	private ProjectDatabaseDao dao;

	@Before
	public void setUp() {
		project = ProjectFixtures.helloWorld();
		databaseManager = PowerMockito.mock(DatabaseManager.class);
		dao = PowerMockito.spy(new ProjectDatabaseDao(databaseManager));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSave() {
		dao.save(project);

		ArgumentCaptor<ProjectRecord> captor = ArgumentCaptor.forClass(ProjectRecord.class);
		Mockito.verify(databaseManager).save(captor.capture());
		assertDeepEquals(project, captor.getValue().convert());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldListAllProjectNames() {
		PowerMockito.doReturn(Arrays.asList("4", "2")).when(dao).getAllNames();
		assertDeepEquals(dao.getProjectNames(), "4", "2");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldGetProjectByName() {
		PowerMockito.doReturn(project).when(dao).getByName("42");
		assertSame(project, dao.getProject("42"));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRemoveProjectByName() {
		PowerMockito.doReturn(project).when(dao).getByName("42");
		dao.removeProject("42");

		ArgumentCaptor<ProjectRecord> captor = ArgumentCaptor.forClass(ProjectRecord.class);
		Mockito.verify(databaseManager).delete(captor.capture(), any(Runnable.class));
		assertDeepEquals(project, captor.getValue().convert());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRemoveDependentEntitiesOnRemove() {
		PowerMockito.doReturn(project).when(dao).getByName("42");
		dao.removeProject("42");

		Query<ProjectRecord> query = PowerMockito.mock(Query.class);
		PowerMockito.doReturn(query).when(dao).createRecordQuery(anyString());
		captureBeforeRemove().run();
		verifyDelete("MetricResult", "module.projectResult.project.name");
		verifyDelete("Module", "projectResult.project.name");
		verifyDelete("ProjectResult", "project.name");
		Mockito.verify(query, Mockito.times(3)).setParameter("projectName", "42");
		Mockito.verify(query, Mockito.times(3)).executeUpdate();
	}

	private Runnable captureBeforeRemove() {
		ArgumentCaptor<Runnable> captor = ArgumentCaptor.forClass(Runnable.class);
		Mockito.verify(databaseManager).delete(any(ProjectRecord.class), captor.capture());
		return captor.getValue();
	}

	private void verifyDelete(String table, String projectNameField) {
		String queryText = "DELETE FROM " + table + " t WHERE t." + projectNameField + " = :projectName";
		Mockito.verify(dao).createRecordQuery(queryText);
	}
}