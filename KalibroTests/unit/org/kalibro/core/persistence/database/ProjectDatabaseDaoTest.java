package org.kalibro.core.persistence.database;

import static org.junit.Assert.*;
import static org.kalibro.core.model.ProjectFixtures.*;
import static org.powermock.api.mockito.PowerMockito.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.model.Project;
import org.kalibro.core.persistence.database.entities.ProjectRecord;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mockito;

public class ProjectDatabaseDaoTest extends KalibroTestCase {

	private Project project;
	private DatabaseManager databaseManager;

	private ProjectDatabaseDao dao;

	@Before
	public void setUp() {
		project = helloWorld();
		databaseManager = mock(DatabaseManager.class);
		dao = spy(new ProjectDatabaseDao(databaseManager));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSave() {
		doReturn(new ArrayList<String>()).when(dao).getAllNames();
		dao.save(project);

		ArgumentCaptor<ProjectRecord> captor = ArgumentCaptor.forClass(ProjectRecord.class);
		Mockito.verify(databaseManager).save(captor.capture());
		assertDeepEquals(project, captor.getValue().convert());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldListAllProjectNames() {
		doReturn(Arrays.asList("4", "2")).when(dao).getAllNames();
		assertDeepEquals(dao.getProjectNames(), "4", "2");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldGetProjectByName() {
		doReturn(project).when(dao).getByName("42");
		assertSame(project, dao.getProject("42"));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRemoveProjectByNameAndDependentEntities() {
		doReturn(project).when(dao).getByName("42");
		Query<?> query = mock(Query.class);
		mockQuery("MetricResult", "module.projectResult.project.name", query);
		mockQuery("Module", "projectResult.project.name", query);
		mockQuery("ProjectResult", "project.name", query);
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

		assertDeepEquals(project, captor.getValue().convert());
	}

	private void mockQuery(String table, String projectNameField, Query<?> query) {
		String queryText = "DELETE FROM " + table + " t WHERE t." + projectNameField + " = :projectName";
		doReturn(query).when(dao).createRecordQuery(queryText);
	}
}