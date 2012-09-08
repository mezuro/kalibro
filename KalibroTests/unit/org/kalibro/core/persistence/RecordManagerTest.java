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

public class RecordManagerTest extends TestCase {

	private static final String MERGED = "RecordManagerTest merged";
	private static final String UNMERGED = "RecordManagerTest unmerged";

	private EntityManager entityManager;
	private EntityTransaction transaction;

	private RecordManager recordManager;

	@Before
	public void setUp() {
		entityManager = mock(EntityManager.class);
		transaction = mock(EntityTransaction.class);
		recordManager = new RecordManager(entityManager);
		when(entityManager.getTransaction()).thenReturn(transaction);
		when(entityManager.merge(UNMERGED)).thenReturn(MERGED);
		mockCache();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldCreateQuery() {
		Query query = mock(Query.class);
		when(entityManager.createQuery("42")).thenReturn(query);
		assertSame(query, recordManager.createQuery("42"));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldClearEntityManagerWhenCreatingQuery() {
		recordManager.createQuery("42");
		verify(entityManager).clear();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldCreateTypedQuery() {
		TypedQuery<String> query = mock(TypedQuery.class);
		when(entityManager.createQuery("42", String.class)).thenReturn(query);
		assertSame(query, recordManager.createQuery("42", String.class));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldClearEntityManagerWhenCreatingTypedQuery() {
		recordManager.createQuery("42", String.class);
		verify(entityManager).clear();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldMergeOnPersist() {
		assertEquals(MERGED, recordManager.persist(UNMERGED));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldMergeBeforeRemove() {
		recordManager.remove(UNMERGED);
		verify(entityManager).remove(MERGED);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSaveSingleEntityAsUnitaryList() throws Exception {
		recordManager = spy(recordManager);
		recordManager.save("42");
		verifyPrivate(recordManager).invoke("save", Arrays.asList("42"));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSaveList() {
		List<String> merged = recordManager.save(Arrays.asList(UNMERGED, UNMERGED, UNMERGED));
		assertEquals(Arrays.asList(MERGED, MERGED, MERGED), merged);

		InOrder order = Mockito.inOrder(transaction, entityManager, transaction);
		order.verify(transaction).begin();
		order.verify(entityManager, times(3)).persist(MERGED);
		order.verify(transaction).commit();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRemove() {
		recordManager.remove(UNMERGED);
		verify(entityManager).remove(MERGED);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldEvictClassFromCache() {
		Cache cache = mockCache();
		recordManager.evictFromCache(Object.class);
		verify(cache).evict(Object.class);
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
		recordManager.finalize();
		verify(entityManager).close();
	}
}