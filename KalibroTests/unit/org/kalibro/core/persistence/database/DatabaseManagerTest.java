package org.kalibro.core.persistence.database;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroTestCase;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DatabaseManager.class)
public class DatabaseManagerTest extends KalibroTestCase {

	private EntityManager entityManager;
	private EntityTransaction transaction;

	private DatabaseManager connectionManager;

	@Before
	public void setUp() {
		entityManager = PowerMockito.mock(EntityManager.class);
		transaction = PowerMockito.mock(EntityTransaction.class);
		connectionManager = new DatabaseManager(entityManager);
		PowerMockito.when(entityManager.getTransaction()).thenReturn(transaction);
		PowerMockito.when(entityManager.merge("unmerged")).thenReturn("merged");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldCreateQuery() throws Exception {
		Query<String> query = PowerMockito.mock(Query.class);
		TypedQuery<String> nativeQuery = PowerMockito.mock(TypedQuery.class);
		PowerMockito.when(entityManager.createQuery("42", String.class)).thenReturn(nativeQuery);
		PowerMockito.whenNew(Query.class).withArguments(nativeQuery).thenReturn(query);

		assertSame(query, connectionManager.createQuery("42", String.class));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldClearEntityManagerWhenCreatingTypedQuery() {
		connectionManager.createQuery("42", String.class);
		Mockito.verify(entityManager).clear();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldMergeBeforePersist() {
		connectionManager.persist("unmerged");
		Mockito.verify(entityManager).persist("merged");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldMergeBeforeRemove() {
		connectionManager.remove("unmerged");
		Mockito.verify(entityManager).remove("merged");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSaveWithNullBeforeSaveByDefault() throws Exception {
		NullRunnable runnable = mockNullRunnable();
		connectionManager.save("42");
		PowerMockito.verifyPrivate(connectionManager).invoke("save", "42", runnable);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSaveSingleEntityAsUnitaryList() throws Exception {
		NullRunnable runnable = mockNullRunnable();
		connectionManager.save("42");
		PowerMockito.verifyPrivate(connectionManager).invoke("save", Arrays.asList("42"), runnable);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSaveListWithNullBeforeSaveByDefault() throws Exception {
		List<String> list = Arrays.asList("4", "2", "42");
		NullRunnable runnable = mockNullRunnable();
		connectionManager.save(list);
		PowerMockito.verifyPrivate(connectionManager).invoke("save", list, runnable);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSaveListWithBeforeBlock() {
		Runnable beforeSave = PowerMockito.mock(Runnable.class);
		connectionManager.save(Arrays.asList("unmerged", "unmerged", "unmerged"), beforeSave);

		InOrder order = verifyTransactionBeginning(beforeSave);
		order.verify(entityManager, Mockito.times(3)).persist("merged");
		order.verify(transaction).commit();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testDelete() throws Exception {
		NullRunnable runnable = mockNullRunnable();
		connectionManager.delete("42");
		PowerMockito.verifyPrivate(connectionManager).invoke("delete", "42", runnable);
	}

	private NullRunnable mockNullRunnable() throws Exception {
		connectionManager = PowerMockito.spy(connectionManager);
		NullRunnable runnable = new NullRunnable();
		PowerMockito.whenNew(NullRunnable.class).withNoArguments().thenReturn(runnable);
		return runnable;
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testDeleteWithBeforeBlock() {
		Runnable beforeDelete = PowerMockito.mock(Runnable.class);
		connectionManager.delete("unmerged", beforeDelete);

		InOrder order = verifyTransactionBeginning(beforeDelete);
		order.verify(entityManager).remove("merged");
		order.verify(transaction).commit();
	}

	private InOrder verifyTransactionBeginning(Runnable beforeBlock) {
		InOrder order = Mockito.inOrder(transaction, beforeBlock, entityManager, transaction);
		order.verify(transaction).begin();
		order.verify(beforeBlock).run();
		return order;
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldCloseEntityManagerOnFinalize() throws Throwable {
		connectionManager.finalize();
		Mockito.verify(entityManager).close();
	}
}