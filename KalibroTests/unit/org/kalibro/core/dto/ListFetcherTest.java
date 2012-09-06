package org.kalibro.core.dto;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.kalibro.TestCase;

public class ListFetcherTest extends TestCase {

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldCreateListProxy() {
		PerfectNumbersFetcher fetcher = new PerfectNumbersFetcher();
		List<Integer> proxy = fetcher.createProxy();
		assertEquals(proxy, fetcher.createProxy());
	}
}