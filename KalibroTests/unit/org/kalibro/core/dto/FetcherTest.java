package org.kalibro.core.dto;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.TestCase;

public class FetcherTest extends TestCase {

	private boolean fetched;
	private List<Integer> proxy;

	@Before
	public void setUp() {
		fetched = false;
		proxy = new ListFetcher().createProxy(List.class);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldFetchOnlyOnFirstInvocation() {
		assertFalse(fetched);
		proxy.size();
		assertTrue(fetched);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldFetchOnlyOnce() {
		proxy.size();
		assertTrue(fetched);

		fetched = false;
		proxy.hashCode();
		assertFalse(fetched);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void proxyShouldActAsRealObject() {
		assertEquals(3, proxy.size());
		assertEquals(6, proxy.get(0).intValue());
		assertEquals(1, proxy.indexOf(28));
		assertTrue(proxy.contains(42));
	}

	@SuppressWarnings("rawtypes")
	private class ListFetcher extends Fetcher<List> {

		@Override
		protected List<Integer> fetch() {
			fetched = true;
			return Arrays.asList(6, 28, 42);
		}
	}
}