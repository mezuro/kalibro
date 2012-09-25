package org.kalibro.core.persistence;

import static org.junit.Assert.*;
import static org.kalibro.core.model.ConfigurationFixtures.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.Configuration;
import org.kalibro.Project;
import org.kalibro.core.persistence.record.ConfigurationRecord;
import org.kalibro.tests.UnitTest;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ConfigurationDatabaseDao.class)
public class ConfigurationDatabaseDaoTest extends UnitTest {

	private Configuration configuration;
	private RecordManager recordManager;

	private ConfigurationDatabaseDao dao;

	@Before
	public void setUp() {
		configuration = newConfiguration("cbo", "lcom4");
		recordManager = mock(RecordManager.class);
		dao = spy(new ConfigurationDatabaseDao(recordManager));
	}

	@Test
	public void shouldGetAll() {
		List<Configuration> all = mock(List.class);
		doReturn(all).when(dao).allOrderedByName();
		assertSame(all, dao.all());
	}

	@Test
	public void shouldSave() {
		when(recordManager.save(any())).thenReturn(new ConfigurationRecord(configuration));
		dao.save(configuration);

		ArgumentCaptor<ConfigurationRecord> captor = ArgumentCaptor.forClass(ConfigurationRecord.class);
		Mockito.verify(recordManager).save(captor.capture());
		assertDeepEquals(configuration, captor.getValue().convert());
	}

	@Test
	public void shouldListAllConfigurationNames() {
		doReturn(Arrays.asList("4", "2")).when(dao).getAllNames();
		assertDeepList(dao.getConfigurationNames(), "4", "2");
	}

	@Test
	public void shouldConfirmConfiguration() {
		doReturn(true).when(dao).hasEntity(CONFIGURATION_NAME);
		assertTrue(dao.hasConfiguration(CONFIGURATION_NAME));

		doReturn(false).when(dao).hasEntity(CONFIGURATION_NAME);
		assertFalse(dao.hasConfiguration(CONFIGURATION_NAME));
	}

	@Test
	public void shouldGetConfigurationByName() {
		doReturn(configuration).when(dao).getByName("42");
		assertSame(configuration, dao.getConfiguration("42"));
	}

	@Test
	public void shouldRemoveConfigurationByName() {
		doNothing().when(dao).deleteById(42L);
		dao.delete(42L);
		verify(dao).deleteById(42L);
	}

	@Test
	public void shouldGetConfigurationByProjectName() throws Exception {
		Project project = mock(Project.class);
		ProjectDatabaseDao projectDao = mock(ProjectDatabaseDao.class);
		whenNew(ProjectDatabaseDao.class).withArguments(recordManager).thenReturn(projectDao);
		when(projectDao.getProject("42")).thenReturn(project);
		when(project.getConfigurationName()).thenReturn("4242");
		doReturn(configuration).when(dao).getByName("4242");

		assertSame(configuration, dao.getConfigurationFor("42"));
	}
}