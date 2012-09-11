package org.kalibro.core.persistence;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

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
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldGetById() {
		when(entityManager.find(Integer.class, 28L)).thenReturn(42);
		assertEquals(42, recordManager.getById(28L, Integer.class).intValue());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldCreateQuery() {
		Query query = mock(Query.class);
		when(entityManager.createQuery("42")).thenReturn(query);
		assertSame(query, recordManager.createQuery("42"));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldCreateTypedQuery() {
		TypedQuery<String> query = mock(TypedQuery.class);
		when(entityManager.createQuery("42", String.class)).thenReturn(query);
		assertSame(query, recordManager.createQuery("42", String.class));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldMergeOnSave() {
		assertEquals(MERGED, recordManager.save(UNMERGED));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldMergeOnSaveList() {
		List<String> merged = recordManager.save(Arrays.asList(UNMERGED, UNMERGED, UNMERGED));
		assertEquals(Arrays.asList(MERGED, MERGED, MERGED), merged);

		InOrder order = Mockito.inOrder(transaction, entityManager, transaction);
		order.verify(transaction).begin();
		order.verify(entityManager, times(3)).persist(MERGED);
		order.verify(transaction).commit();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldBeginTransaction() {
		recordManager.beginTransaction();
		verify(transaction).begin();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldCommitTransaction() {
		recordManager.commitTransaction();
		verify(transaction).commit();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldCloseEntityManagerOnFinalize() throws Throwable {
		recordManager.finalize();
		verify(entityManager).close();
	}
}