package org.kalibro.core.concurrent;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;

public class ConcurrentInvocationHandlerTest extends KalibroTestCase {

	private long beforeId, testId, getterId;

	private IdGetter getter;

	@Before
	public void setUp() {
		getter = ConcurrentInvocationHandler.createProxy(new ThreadIdGetter(), IdGetter.class);
		beforeId = getThreadId();
		getterId = getter.getId();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRunMethodInSameThread() {
		testId = getThreadId();
		assertDifferent(getterId, beforeId, testId);
		assertEquals(getterId, getter.getId());
	}

	private long getThreadId() {
		return Thread.currentThread().getId();
	}

	interface IdGetter {

		long getId();
	}

	class ThreadIdGetter implements IdGetter {

		@Override
		public long getId() {
			return getThreadId();
		}
	}
}