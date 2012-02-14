package org.kalibro.core.persistence.database;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.model.Configuration;
import org.kalibro.core.model.ConfigurationFixtures;
import org.kalibro.core.model.Project;
import org.kalibro.core.persistence.database.ConfigurationDatabaseDao;
import org.kalibro.core.persistence.database.DatabaseManager;
import org.kalibro.core.persistence.database.ProjectDatabaseDao;
import org.kalibro.core.persistence.database.entities.ConfigurationRecord;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ConfigurationDatabaseDao.class)
public class ConfigurationDatabaseDaoTest extends KalibroTestCase {

	private Configuration configuration;
	private DatabaseManager databaseManager;

	private ConfigurationDatabaseDao dao;

	@Before
	public void setUp() {
		configuration = ConfigurationFixtures.kalibroConfiguration();
		databaseManager = PowerMockito.mock(DatabaseManager.class);
		dao = PowerMockito.spy(new ConfigurationDatabaseDao(databaseManager));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSave() {
		dao.save(configuration);

		ArgumentCaptor<ConfigurationRecord> captor = ArgumentCaptor.forClass(ConfigurationRecord.class);
		Mockito.verify(databaseManager).save(captor.capture());
		assertDeepEquals(configuration, captor.getValue().convert());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldListAllConfigurationNames() {
		PowerMockito.doReturn(Arrays.asList("4", "2")).when(dao).getAllNames();
		assertDeepEquals(dao.getConfigurationNames(), "4", "2");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldGetConfigurationByName() {
		PowerMockito.doReturn(configuration).when(dao).getByName("42");
		assertSame(configuration, dao.getConfiguration("42"));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRemoveConfigurationByName() {
		PowerMockito.doReturn(configuration).when(dao).getByName("42");
		dao.removeConfiguration("42");

		ArgumentCaptor<ConfigurationRecord> captor = ArgumentCaptor.forClass(ConfigurationRecord.class);
		Mockito.verify(databaseManager).delete(captor.capture());
		assertDeepEquals(configuration, captor.getValue().convert());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldGetConfigurationByProjectName() throws Exception {
		Project project = PowerMockito.mock(Project.class);
		ProjectDatabaseDao projectDao = PowerMockito.mock(ProjectDatabaseDao.class);
		PowerMockito.whenNew(ProjectDatabaseDao.class).withArguments(databaseManager).thenReturn(projectDao);
		PowerMockito.when(projectDao.getProject("42")).thenReturn(project);
		PowerMockito.when(project.getConfigurationName()).thenReturn("4242");
		PowerMockito.doReturn(configuration).when(dao).getByName("4242");

		assertSame(configuration, dao.getConfigurationFor("42"));
	}
}