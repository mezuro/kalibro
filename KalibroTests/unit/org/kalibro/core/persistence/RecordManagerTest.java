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
import org.mockito.verification.VerificationMode;
import org.powermock.reflect.Whitebox;

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
	public void shouldExecuteUpdateQuery() throws Exception {
		Query updateQuery = mock(Query.class);
		recordManager.executeUpdate(updateQuery);
		verifyWithinTransaction(updateQuery, "executeUpdate");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldMergeAndSave() throws Exception {
		assertEquals(MERGED, recordManager.save(UNMERGED));
		verifyWithinTransaction(entityManager, "persist", MERGED);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldMergeAndSaveList() throws Exception {
		List<String> merged = recordManager.save(Arrays.asList(UNMERGED, UNMERGED, UNMERGED));
		assertEquals(Arrays.asList(MERGED, MERGED, MERGED), merged);
		verifyWithinTransaction(entityManager, times(3), "persist", MERGED);
	}

	private void verifyWithinTransaction(Object mock, String method, Object... arguments) throws Exception {
		verifyWithinTransaction(mock, once(), method, arguments);
	}

	private void verifyWithinTransaction(Object mock, VerificationMode mode, String method, Object... arguments)
		throws Exception {
		InOrder order = Mockito.inOrder(transaction, mock, transaction);
		order.verify(transaction).begin();
		Whitebox.invokeMethod(order.verify(mock, mode), method, arguments);
		order.verify(transaction).commit();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldCloseEntityManagerOnFinalize() throws Throwable {
		recordManager.finalize();
		verify(entityManager).close();
	}
}