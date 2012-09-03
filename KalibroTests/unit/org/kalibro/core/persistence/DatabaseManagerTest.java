package org.kalibro.core.persistence;

import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.*;

import java.util.Arrays;
import java.util.List;

import javax.persistence.*;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.TestCase;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;

public class DatabaseManagerTest extends TestCase {

	private static final String MERGED = "DatabaseManagerTest merged";
	private static final String UNMERGED = "DatabaseManagerTest unmerged";

	private EntityManager entityManager;
	private EntityTransaction transaction;

	private DatabaseManager databaseManager;

	@Before
	public void setUp() {
		entityManager = mock(EntityManager.class);
		transaction = mock(EntityTransaction.class);
		databaseManager = new DatabaseManager(entityManager);
		when(entityManager.getTransaction()).thenReturn(transaction);
		when(entityManager.merge(UNMERGED)).thenReturn(MERGED);
		mockCache();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldCreateQuery() {
		TypedQuery<String> nativeQuery = mock(TypedQuery.class);
		when(entityManager.createQuery("42", String.class)).thenReturn(nativeQuery);

		Query<String> query = databaseManager.createQuery("42", String.class);
		assertSame(nativeQuery, Whitebox.getInternalState(query, TypedQuery.class));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldClearEntityManagerWhenCreatingTypedQuery() {
		databaseManager.createQuery("42", String.class);
		Mockito.verify(entityManager).clear();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldMergeOnPersist() {
		assertEquals(MERGED, databaseManager.persist(UNMERGED));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldMergeBeforeRemove() {
		databaseManager.remove(UNMERGED);
		Mockito.verify(entityManager).remove(MERGED);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSaveSingleEntityAsUnitaryList() throws Exception {
		databaseManager = spy(databaseManager);
		databaseManager.save("42");
		verifyPrivate(databaseManager).invoke("save", Arrays.asList("42"));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSaveList() {
		List<String> merged = databaseManager.save(Arrays.asList(UNMERGED, UNMERGED, UNMERGED));
		assertEquals(Arrays.asList(MERGED, MERGED, MERGED), merged);

		InOrder order = Mockito.inOrder(transaction, entityManager, transaction);
		order.verify(transaction).begin();
		order.verify(entityManager, Mockito.times(3)).persist(MERGED);
		order.verify(transaction).commit();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldDelete() {
		databaseManager.delete(UNMERGED);

		InOrder order = Mockito.inOrder(transaction, entityManager, transaction);
		order.verify(transaction).begin();
		order.verify(entityManager).remove(MERGED);
		order.verify(transaction).commit();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldEvictClassFromCache() {
		Cache cache = mockCache();
		databaseManager.evictFromCache(Object.class);
		Mockito.verify(cache).evict(Object.class);
	}

	private Cache mockCache() {
		Cache cache = mock(Cache.class);
		EntityManagerFactory entityManagerFactory = mock(EntityManagerFactory.class);
		when(entityManager.getEntityManagerFactory()).thenReturn(entityManagerFactory);
		when(entityManagerFactory.getCache()).thenReturn(cache);
		return cache;
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldCloseEntityManagerOnFinalize() throws Throwable {
		databaseManager.finalize();
		Mockito.verify(entityManager).close();
	}
}