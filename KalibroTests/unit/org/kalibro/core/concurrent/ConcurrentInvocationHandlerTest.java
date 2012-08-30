package org.kalibro.core.concurrent;

import static org.junit.Assert.*;

import java.lang.reflect.Constructor;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.TestCase;
import org.powermock.reflect.Whitebox;

public class ConcurrentInvocationHandlerTest extends TestCase {

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

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldEndThreadOnFinalize() throws Throwable {
		ConcurrentInvocationHandler handler = createHandler();
		handler.finalize();
		assertFalse((Boolean) Whitebox.getInternalState(handler, "running"));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldHaveDescription() throws Exception {
		assertEquals("handling invocations for java.lang.String", "" + createHandler());
	}

	private ConcurrentInvocationHandler createHandler() throws Exception {
		Class<ConcurrentInvocationHandler> handlerClass = ConcurrentInvocationHandler.class;
		Constructor<ConcurrentInvocationHandler> constructor = handlerClass.getDeclaredConstructor(Object.class);
		constructor.setAccessible(true);
		return constructor.newInstance("");
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