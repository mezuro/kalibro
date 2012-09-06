package org.kalibro.core.dto;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.UtilityClassTest;

public class ProxyFactoryTest extends UtilityClassTest {

	private int loadCount;
	private List<Integer> proxy;

	@Before
	public void setUp() {
		loadCount = 0;
		proxy = ProxyFactory.lazyLoadProxy(List.class, new PerfectNumbersLoader());
	}

	@Override
	protected Class<?> utilityClass() {
		return ProxyFactory.class;
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldLoadOnFirstInvocation() {
		assertEquals(0, loadCount);
		proxy.size();
		assertEquals(1, loadCount);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldFetchOnlyOnce() {
		proxy.size();
		proxy.toArray();
		proxy.toString();
		assertEquals(1, loadCount);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void proxyShouldActAsRealObject() {
		assertEquals(3, proxy.size());
		assertEquals(6, proxy.get(0).intValue());
		assertEquals(1, proxy.indexOf(28));
		assertTrue(proxy.contains(496));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldProxyNonFinalClasses() {
		AtomicInteger answer = ProxyFactory.lazyLoadProxy(AtomicInteger.class, new Loader<AtomicInteger>() {

			@Override
			public AtomicInteger load() {
				return new AtomicInteger(42);
			}
		});
		assertEquals(42, answer.intValue());
	}

	private class PerfectNumbersLoader implements Loader<List<Integer>> {

		@Override
		public List<Integer> load() {
			loadCount++;
			return Arrays.asList(6, 28, 496);
		}
	}
}