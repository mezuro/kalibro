package org.kalibro.core.persistence.database;

import static org.junit.Assert.*;

import java.util.Arrays;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.concurrent.Task;
import org.kalibro.core.persistence.database.Query;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

public class QueryTest extends KalibroTestCase {

	private TypedQuery<String> nativeQuery;
	private Query<String> query;

	@Before
	public void setUp() {
		nativeQuery = PowerMockito.mock(TypedQuery.class);
		query = new Query<String>(nativeQuery);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSetParameter() {
		query.setParameter("parameter_name", "42");
		Mockito.verify(nativeQuery).setParameter("parameter_name", "42");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldGetSingleResult() {
		PowerMockito.when(nativeQuery.getSingleResult()).thenReturn("42");
		assertEquals("42", query.getSingleResult());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldChangeErrorMessage() {
		PowerMockito.when(nativeQuery.getSingleResult()).thenThrow(new NoResultException("Original message"));
		checkException(new Task() {

			@Override
			public void perform() {
				query.getSingleResult();
			}
		}, NoResultException.class, "No entity found");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldCustomizeErrorMessage() {
		PowerMockito.when(nativeQuery.getSingleResult()).thenThrow(new NoResultException("Original message"));
		checkException(new Task() {

			@Override
			public void perform() {
				query.getSingleResult("Customized error message");
			}
		}, NoResultException.class, "Customized error message");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldGetResultList() {
		PowerMockito.when(nativeQuery.getResultList()).thenReturn(Arrays.asList("4", "2"));
		assertDeepEquals(query.getResultList(), "4", "2");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldExecuteUpdate() {
		query.executeUpdate();
		Mockito.verify(nativeQuery).executeUpdate();
	}
}