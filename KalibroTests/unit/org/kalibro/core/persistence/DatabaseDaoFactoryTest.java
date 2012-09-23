package org.kalibro.core.persistence;

import static org.eclipse.persistence.config.PersistenceUnitProperties.*;
import static org.junit.Assert.*;

import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.DatabaseSettings;
import org.kalibro.core.Environment;
import org.kalibro.tests.UnitTest;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({DatabaseDaoFactory.class, Persistence.class})
public class DatabaseDaoFactoryTest extends UnitTest {

	private EntityManagerFactory managerFactory;
	private BaseToolDatabaseDao baseToolDao;
	private DatabaseSettings settings;

	private DatabaseDaoFactory daoFactory;

	@Before
	public void setUp() throws Exception {
		mockPersistence();
		baseToolDao = mock(BaseToolDatabaseDao.class);
		whenNew(BaseToolDatabaseDao.class).withArguments(any()).thenReturn(baseToolDao);
		settings = new DatabaseSettings();
		daoFactory = new DatabaseDaoFactory(settings);
	}

	private void mockPersistence() {
		managerFactory = mock(EntityManagerFactory.class);
		mockStatic(Persistence.class);
		when(Persistence.createEntityManagerFactory(eq("Kalibro"), any(Map.class))).thenReturn(managerFactory);
	}

	@Test
	public void shouldSetPersistencePropertiesAccordingToDatabaseSettings() {
		@SuppressWarnings("rawtypes")
		ArgumentCaptor<Map> captor = ArgumentCaptor.forClass(Map.class);
		verifyStatic();
		Persistence.createEntityManagerFactory(eq("Kalibro"), captor.capture());
		Map<String, String> properties = captor.getValue();

		assertEquals(Environment.ddlGeneration(), properties.get(DDL_GENERATION));
		assertEquals(settings.getDatabaseType().getDriverClassName(), properties.get(JDBC_DRIVER));
		assertEquals(settings.getJdbcUrl(), properties.get(JDBC_URL));
		assertEquals(settings.getUsername(), properties.get(JDBC_USER));
		assertEquals(settings.getPassword(), properties.get(JDBC_PASSWORD));
	}

	@Test
	public void shouldCreateDatabaseDaos() {
		assertSame(baseToolDao, daoFactory.createBaseToolDao());
		assertClassEquals(ConfigurationDatabaseDao.class, daoFactory.createConfigurationDao());
		assertClassEquals(MetricConfigurationDatabaseDao.class, daoFactory.createMetricConfigurationDao());
		assertClassEquals(ModuleResultDatabaseDao.class, daoFactory.createModuleResultDao());
		assertClassEquals(ProjectDatabaseDao.class, daoFactory.createProjectDao());
		assertClassEquals(ProjectResultDatabaseDao.class, daoFactory.createProjectResultDao());
		assertClassEquals(ReadingDatabaseDao.class, daoFactory.createReadingDao());
		assertClassEquals(ReadingGroupDatabaseDao.class, daoFactory.createReadingGroupDao());
	}

	@Test
	public void shouldSaveBaseTools() {
		Mockito.verify(baseToolDao).saveBaseTools();
	}
}