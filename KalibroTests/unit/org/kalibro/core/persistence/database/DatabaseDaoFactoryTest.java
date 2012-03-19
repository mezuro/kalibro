package org.kalibro.core.persistence.database;

import static org.mockito.Matchers.*;

import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.settings.DatabaseSettings;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Persistence.class, Seeds.class})
public class DatabaseDaoFactoryTest extends KalibroTestCase {

	private DatabaseDaoFactory daoFactory;
	private EntityManagerFactory managerFactory;

	@Before
	public void setUp() {
		mockPersistence();
		PowerMockito.mockStatic(Seeds.class);
		daoFactory = new DatabaseDaoFactory();
	}

	private void mockPersistence() {
		managerFactory = PowerMockito.mock(EntityManagerFactory.class);
		PowerMockito.mockStatic(Persistence.class);
		PowerMockito.when(Persistence.createEntityManagerFactory(eq("Kalibro"), any(Map.class)))
			.thenReturn(managerFactory);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkDefaultProperties() {
		@SuppressWarnings("rawtypes")
		ArgumentCaptor<Map> captor = ArgumentCaptor.forClass(Map.class);
		PowerMockito.verifyStatic();
		Persistence.createEntityManagerFactory(eq("Kalibro"), captor.capture());
		assertDeepEquals(new DatabaseSettings().toPersistenceProperties(), captor.getValue());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkDaoCreation() {
		assertClassEquals(BaseToolDatabaseDao.class, daoFactory.getBaseToolDao());
		assertClassEquals(ConfigurationDatabaseDao.class, daoFactory.getConfigurationDao());
		assertClassEquals(MetricConfigurationDatabaseDao.class, daoFactory.getMetricConfigurationDao());
		assertClassEquals(ProjectDatabaseDao.class, daoFactory.getProjectDao());
		assertClassEquals(ProjectResultDatabaseDao.class, daoFactory.getProjectResultDao());
		assertClassEquals(ModuleResultDatabaseDao.class, daoFactory.getModuleResultDao());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldAssertDatabaseIsSeeded() {
		PowerMockito.verifyStatic();
		Seeds.saveSeedsIfFirstTime(any(DatabaseManager.class));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldCloseManagerFactoryWhenFinalize() throws Throwable {
		daoFactory.finalize();
		Mockito.verify(managerFactory).close();
	}
}