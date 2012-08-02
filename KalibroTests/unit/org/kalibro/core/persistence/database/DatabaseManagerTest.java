package org.kalibro.core.persistence.database;

import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.*;

import java.util.Arrays;

import javax.persistence.*;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;

public class DatabaseManagerTest extends KalibroTestCase {

	private EntityManager entityManager;
	private EntityTransaction transaction;

	private DatabaseManager databaseManager;

	@Before
	public void setUp() {
		entityManager = mock(EntityManager.class);
		transaction = mock(EntityTransaction.class);
		databaseManager = new DatabaseManager(entityManager);
		when(entityManager.getTransaction()).thenReturn(transaction);
		when(entityManager.merge("unmerged")).thenReturn("merged");
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
	public void shouldMergeBeforePersist() {
		databaseManager.persist("unmerged");
		Mockito.verify(entityManager).persist("merged");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldMergeBeforeRemove() {
		databaseManager.remove("unmerged");
		Mockito.verify(entityManager).remove("merged");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSaveSingleEntityAsUnitaryList() throws Exception {
		databaseManager = spy(databaseManager);
		databaseManager.save("42");
		verifyPrivate(databaseManager).invoke("save", Arrays.asList("42"));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSaveList() {
		databaseManager.save(Arrays.asList("unmerged", "unmerged", "unmerged"));

		InOrder order = Mockito.inOrder(transaction, entityManager, transaction);
		order.verify(transaction).begin();
		order.verify(entityManager, Mockito.times(3)).persist("merged");
		order.verify(transaction).commit();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldDelete() {
		databaseManager.delete("unmerged");

		InOrder order = Mockito.inOrder(transaction, entityManager, transaction);
		order.verify(transaction).begin();
		order.verify(entityManager).remove("merged");
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