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
import org.kalibro.tests.UnitTest;
import org.mockito.ArgumentCaptor;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
@PrepareForTest({DatabaseDaoFactory.class, KalibroSettings.class, Persistence.class})
public class DatabaseDaoFactoryTest extends UnitTest {

	private DatabaseSettings settings;
	private RecordManager recordManager;
	private EntityManagerFactory entityManagerFactory;

	private DatabaseDaoFactory factory;

	@Before
	public void setUp() throws Exception {
		Whitebox.setInternalState(DatabaseDaoFactory.class, "currentSettings", (DatabaseSettings) null);
		Whitebox.setInternalState(DatabaseDaoFactory.class, "entityManagerFactory", (EntityManagerFactory) null);
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
		recordManager = mock(RecordManager.class);
		entityManagerFactory = mock(EntityManagerFactory.class);
		EntityManager entityManager = mock(EntityManager.class);

		mockStatic(Persistence.class);
		when(Persistence.createEntityManagerFactory(eq("Kalibro"), any(Map.class))).thenReturn(entityManagerFactory);
		when(entityManagerFactory.isOpen()).thenReturn(true);
		when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
		when(entityManager.getEntityManagerFactory()).thenReturn(entityManagerFactory);
		whenNew(RecordManager.class).withArguments(entityManager).thenReturn(recordManager);
	}

	@Test
	public void shouldCreateRecordManager() {
		assertSame(recordManager, DatabaseDaoFactory.createRecordManager());
	}

	@Test
	public void shouldSetPersistencePropertiesAccordingToDatabaseSettings() {
		Map<String, String> properties = capturePersistenceProperties();
		assertEquals(CREATE_ONLY, properties.get(DDL_GENERATION));
		assertEquals(settings.getDatabaseType().getDriverClassName(), properties.get(JDBC_DRIVER));
		assertEquals(settings.getJdbcUrl(), properties.get(JDBC_URL));
		assertEquals(settings.getUsername(), properties.get(JDBC_USER));
		assertEquals(settings.getPassword(), properties.get(JDBC_PASSWORD));
		assertEquals(PersistenceLogger.class.getName(), properties.get(LOGGING_LOGGER));
		assertEquals(DatabaseImport.class.getName(), properties.get(SESSION_CUSTOMIZER));
	}

	@SuppressWarnings("rawtypes")
	private Map<String, String> capturePersistenceProperties() {
		ArgumentCaptor<Map> captor = ArgumentCaptor.forClass(Map.class);
		verifyStatic();
		Persistence.createEntityManagerFactory(eq("Kalibro"), captor.capture());
		return captor.getValue();
	}

	@Test
	public void shouldClosePreviousEntityManagerIfOpen() {
		DatabaseSettings newSettings = new DatabaseSettings();
		newSettings.setPassword("x");
		new DatabaseDaoFactory(settings);
		new DatabaseDaoFactory(newSettings);

		verify(entityManagerFactory, times(1)).close();
	}

	@Test
	public void shouldEnsureOpenEntityManagerFactoryBeforeCreatingRecordManager() {
		when(entityManagerFactory.isOpen()).thenReturn(false);
		DatabaseDaoFactory.createRecordManager();

		verifyStatic(times(2));
		Persistence.createEntityManagerFactory(anyString(), any(Map.class));
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
		whenNew(daoClass).withNoArguments().thenReturn(dao);
		String methodName = "create" + daoClass.getSimpleName().replace("Database", "");
		assertSame("Unexpected return of " + methodName, dao, Whitebox.invokeMethod(factory, methodName));
	}
}