package org.kalibro.core.dto;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.TestCase;

public class FetcherTest extends TestCase {

	private List<Integer> proxy;
	private PerfectNumbersFetcher fetcher;

	@Before
	public void setUp() {
		fetcher = new PerfectNumbersFetcher();
		proxy = fetcher.createProxy();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldFetchOnlyOnFirstInvocation() {
		assertEquals(0, fetcher.fetchCount());
		proxy.size();
		assertEquals(1, fetcher.fetchCount());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldFetchOnlyOnce() {
		proxy.size();
		assertEquals(1, fetcher.fetchCount());

		proxy.hashCode();
		assertEquals(1, fetcher.fetchCount());

		proxy.get(0);
		assertEquals(1, fetcher.fetchCount());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void proxyShouldActAsRealObject() {
		assertEquals(3, proxy.size());
		assertEquals(6, proxy.get(0).intValue());
		assertEquals(1, proxy.indexOf(28));
		assertTrue(proxy.contains(496));
	}
}