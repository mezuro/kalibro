package org.kalibro.core.persistence.database;

import static org.junit.Assert.*;
import static org.kalibro.core.model.ConfigurationFixtures.*;
import static org.mockito.Matchers.*;
import static org.powermock.api.mockito.PowerMockito.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.model.Configuration;
import org.kalibro.core.model.Project;
import org.kalibro.core.persistence.database.entities.ConfigurationRecord;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
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
		configuration = newConfiguration("cbo", "lcom4");
		databaseManager = mock(DatabaseManager.class);
		dao = spy(new ConfigurationDatabaseDao(databaseManager));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSave() {
		when(databaseManager.save(any())).thenReturn(new ConfigurationRecord(configuration));
		dao.save(configuration);

		ArgumentCaptor<ConfigurationRecord> captor = ArgumentCaptor.forClass(ConfigurationRecord.class);
		Mockito.verify(databaseManager).save(captor.capture());
		assertDeepEquals(configuration, captor.getValue().convert());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldListAllConfigurationNames() {
		doReturn(Arrays.asList("4", "2")).when(dao).getAllNames();
		assertDeepEquals(dao.getConfigurationNames(), "4", "2");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldConfirmConfiguration() {
		doReturn(true).when(dao).hasEntity(CONFIGURATION_NAME);
		assertTrue(dao.hasConfiguration(CONFIGURATION_NAME));

		doReturn(false).when(dao).hasEntity(CONFIGURATION_NAME);
		assertFalse(dao.hasConfiguration(CONFIGURATION_NAME));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldGetConfigurationByName() {
		doReturn(configuration).when(dao).getByName("42");
		assertSame(configuration, dao.getConfiguration("42"));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRemoveConfigurationByName() {
		doReturn(configuration).when(dao).getByName("42");
		dao.removeConfiguration("42");

		ArgumentCaptor<ConfigurationRecord> captor = ArgumentCaptor.forClass(ConfigurationRecord.class);
		Mockito.verify(databaseManager).delete(captor.capture());
		assertDeepEquals(configuration, captor.getValue().convert());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldGetConfigurationByProjectName() throws Exception {
		Project project = mock(Project.class);
		ProjectDatabaseDao projectDao = mock(ProjectDatabaseDao.class);
		whenNew(ProjectDatabaseDao.class).withArguments(databaseManager).thenReturn(projectDao);
		when(projectDao.getProject("42")).thenReturn(project);
		when(project.getConfigurationName()).thenReturn("4242");
		doReturn(configuration).when(dao).getByName("4242");

		assertSame(configuration, dao.getConfigurationFor("42"));
	}
}