package org.kalibro.core.persistence;

import static org.eclipse.persistence.config.PersistenceUnitProperties.*;
import static org.junit.Assert.*;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.DatabaseSettings;
import org.kalibro.KalibroSettings;
import org.kalibro.core.Environment;
import org.kalibro.tests.UnitTest;
import org.mockito.ArgumentCaptor;
import org.mockito.stubbing.OngoingStubbing;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
@PrepareForTest({DatabaseDaoFactory.class, KalibroSettings.class, Persistence.class})
public class DatabaseDaoFactoryTest extends UnitTest {

	private DatabaseSettings settings;
	private RecordManager recordManager;

	private DatabaseDaoFactory factory;

	@Before
	public void setUp() throws Exception {
		mockDatabaseSettings();
		mockPersistence();
		factory = new DatabaseDaoFactory();
	}

	private void mockDatabaseSettings() {
		settings = new DatabaseSettings();
		mockStatic(KalibroSettings.class);
		when(KalibroSettings.load()).thenReturn(new KalibroSettings());
	}

	private void mockPersistence() throws Exception {
		EntityManagerFactory entityManagerFactory = mock(EntityManagerFactory.class);
		EntityManager entityManager = mock(EntityManager.class);
		recordManager = mock(RecordManager.class);
		mockStatic(Persistence.class);
		when(Persistence.createEntityManagerFactory(eq("Kalibro"), any(Map.class))).thenReturn(entityManagerFactory);
		when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
		whenNew(RecordManager.class).withArguments(entityManager).thenReturn(recordManager);
	}

	@Test
	public void shouldSetPersistencePropertiesAccordingToDatabaseSettings() {
		Map<String, String> properties = capturePersistenceProperties();
		assertEquals(Environment.ddlGeneration(), properties.get(DDL_GENERATION));
		assertEquals(settings.getDatabaseType().getDriverClassName(), properties.get(JDBC_DRIVER));
		assertEquals(settings.getJdbcUrl(), properties.get(JDBC_URL));
		assertEquals(settings.getUsername(), properties.get(JDBC_USER));
		assertEquals(settings.getPassword(), properties.get(JDBC_PASSWORD));
		assertEquals(PersistenceLogger.class.getName(), properties.get(LOGGING_LOGGER));
	}

	@SuppressWarnings("rawtypes")
	private Map<String, String> capturePersistenceProperties() {
		ArgumentCaptor<Map> captor = ArgumentCaptor.forClass(Map.class);
		verifyStatic();
		Persistence.createEntityManagerFactory(eq("Kalibro"), captor.capture());
		return captor.getValue();
	}

	@Test
	public void shouldCreateDaos() throws Exception {
		shouldCreate(BaseToolDatabaseDao.class);
		shouldCreate(ConfigurationDatabaseDao.class);
		shouldCreate(MetricConfigurationDatabaseDao.class);
		shouldCreate(MetricResultDatabaseDao.class);
		shouldCreate(ModuleResultDatabaseDao.class);
		shouldCreate(ProcessingDatabaseDao.class);
		shouldCreate(ProjectDatabaseDao.class);
		shouldCreate(RangeDatabaseDao.class);
		shouldCreate(ReadingDatabaseDao.class);
		shouldCreate(ReadingGroupDatabaseDao.class);
		shouldCreate(RepositoryDatabaseDao.class);
	}

	private <T> void shouldCreate(Class<T> daoClass) throws Exception {
		T dao = mock(daoClass);
		whenCreate(daoClass).thenReturn(dao);
		String methodName = "create" + daoClass.getSimpleName().replace("Database", "");
		assertSame(dao, Whitebox.invokeMethod(factory, methodName));
	}

	private <T> OngoingStubbing<T> whenCreate(Class<T> daoClass) throws Exception {
		if (daoClass.getDeclaredConstructors()[0].getParameterTypes().length == 0)
			return whenNew(daoClass).withNoArguments();
		return whenNew(daoClass).withArguments(recordManager);
	}
}