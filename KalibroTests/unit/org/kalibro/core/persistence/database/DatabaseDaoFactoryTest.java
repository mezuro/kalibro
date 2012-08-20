package org.kalibro.core.persistence.database;

import static org.eclipse.persistence.config.PersistenceUnitProperties.*;
import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.powermock.api.mockito.PowerMockito.*;

import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.DatabaseSettings;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.Environment;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({DatabaseDaoFactory.class, Persistence.class})
public class DatabaseDaoFactoryTest extends KalibroTestCase {

	private EntityManagerFactory managerFactory;
	private BaseToolDatabaseDao baseToolDao;
	private DatabaseSettings settings;

	private DatabaseDaoFactory daoFactory;

	@Before
	public void setUp() throws Exception {
		mockPersistence();
		settings = new DatabaseSettings();
		baseToolDao = mock(BaseToolDatabaseDao.class);
		whenNew(BaseToolDatabaseDao.class).withArguments(any()).thenReturn(baseToolDao);
		daoFactory = new DatabaseDaoFactory(settings);
	}

	private void mockPersistence() {
		managerFactory = mock(EntityManagerFactory.class);
		mockStatic(Persistence.class);
		when(Persistence.createEntityManagerFactory(eq("Kalibro"), any(Map.class))).thenReturn(managerFactory);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkDefaultProperties() {
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

	@Test(timeout = UNIT_TIMEOUT)
	public void checkDaoCreation() {
		assertSame(baseToolDao, daoFactory.getBaseToolDao());
		assertClassEquals(ConfigurationDatabaseDao.class, daoFactory.getConfigurationDao());
		assertClassEquals(MetricConfigurationDatabaseDao.class, daoFactory.getMetricConfigurationDao());
		assertClassEquals(ProjectDatabaseDao.class, daoFactory.getProjectDao());
		assertClassEquals(ProjectResultDatabaseDao.class, daoFactory.getProjectResultDao());
		assertClassEquals(ModuleResultDatabaseDao.class, daoFactory.getModuleResultDao());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSaveBaseTools() {
		Mockito.verify(baseToolDao).saveBaseTools();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldCloseManagerFactoryWhenFinalize() throws Throwable {
		daoFactory.finalize();
		Mockito.verify(managerFactory).close();
	}
}