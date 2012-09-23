package org.cvsanaly;

import static org.eclipse.persistence.config.PersistenceUnitProperties.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.TestCase;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Persistence.class)
public class CVSAnalyDatabaseFetcherTest extends TestCase {

	private static final File DATABASE_PATH = new File("/tmp/teste");

	private CVSAnalyDatabaseFetcher fetcher;
	private Map<String, String> expectedPersistenceProperites = createExpectedPersistencePropertiesMap();

	private Map<String, String> createExpectedPersistencePropertiesMap() {
		expectedPersistenceProperites = new HashMap<String, String>();
		expectedPersistenceProperites.put(DDL_GENERATION, NONE);
		expectedPersistenceProperites.put(JDBC_DRIVER, "org.sqlite.JDBC");
		expectedPersistenceProperites.put(JDBC_URL, "jdbc:sqlite:/tmp/teste");
		return expectedPersistenceProperites;
	}

	@Before
	public void setUp() {
		fetcher = new CVSAnalyDatabaseFetcher(DATABASE_PATH);
	}

	@Test
	public void testPersistenceProperties() {
		assertDeepEquals(expectedPersistenceProperites, fetcher.getPersistenceProperties());
	}

	@Test
	public void shouldGetResultsFromEntityManager() {
		EntityManagerFactory managerFactory = PowerMockito.mock(EntityManagerFactory.class);
		EntityManager mockManager = Mockito.mock(EntityManager.class);
		Query mockQuery = Mockito.mock(Query.class);

		PowerMockito.mockStatic(Persistence.class);
		PowerMockito.when(Persistence.createEntityManagerFactory(eq("cvsanaly"), any(Map.class)))
			.thenReturn(managerFactory);

		Mockito.when(managerFactory.createEntityManager()).thenReturn(mockManager);
		Mockito.when(mockManager.createNamedQuery(any(String.class))).thenReturn(mockQuery);

		fetcher.getMetricResults();

		Mockito.verify(mockQuery).getResultList();
		Mockito.verify(mockManager).close();
	}

}
