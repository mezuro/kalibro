package org.kalibro.core.concurrent;

import static org.junit.Assert.*;

import java.lang.reflect.Constructor;

import org.junit.Test;
import org.kalibro.TestCase;
import org.powermock.reflect.Whitebox;

public class ConcurrentInvocationHandlerTest extends TestCase {

	@Test
	public void shouldRunMethodInSameThread() {
		IdGetter getter = ConcurrentInvocationHandler.createProxy(new ThreadIdGetter(), IdGetter.class);
		long getterId = getter.getId();
		long testId = getThreadId();
		assertDifferent(getterId, testId);
		assertEquals(getterId, getter.getId());
	}

	@Test
	public void shouldEndThreadOnFinalize() throws Throwable {
		ConcurrentInvocationHandler handler = createHandler();
		handler.finalize();
		assertFalse((Boolean) Whitebox.getInternalState(handler, "running"));
	}

	@Test
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