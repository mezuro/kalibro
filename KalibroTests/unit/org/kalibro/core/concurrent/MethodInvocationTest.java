package org.kalibro.core.concurrent;

import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.*;

import java.lang.reflect.Method;
import java.util.Queue;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class MethodInvocationTest extends KalibroTestCase {

	private static final String STRING = "My string";

	private Method method;
	private MethodInvocation invocation;

	@Before
	public void setUp() throws Exception {
		method = String.class.getMethod("substring", int.class);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldInvoke() throws Throwable {
		createInvocation(3);
		invocation.invoke();
		assertEquals("string", invocation.getResult());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRetrieveIfInvocationWasDone() {
		createInvocation(4);
		assertFalse(invocation.done());
		invocation.invoke();
		assertTrue(invocation.done());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldThrowExceptionWhenRetrievingResult() {
		createInvocation(null);
		invocation.invoke();
		checkException(new Task() {

			@Override
			protected void perform() throws Throwable {
				invocation.getResult();
			}
		}, IllegalArgumentException.class);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldThrowCauseOfInvocationTargetException() {
		createInvocation(-1);
		invocation.invoke();
		checkException(new Task() {

			@Override
			protected void perform() throws Throwable {
				invocation.getResult();
			}
		}, StringIndexOutOfBoundsException.class);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldAddToQueueAndWait() throws Exception {
		createInvocation(0);
		Queue<MethodInvocation> queue = mock(Queue.class);
		when(queue.add(invocation)).thenAnswer(new WaitAndInvokeInOtherThread());
		invocation.addToQueueAndWait(queue);
		Mockito.verify(queue).add(invocation);
	}

	private void createInvocation(Integer index) {
		invocation = new MethodInvocation(STRING, method, index);
	}

	private class WaitAndInvokeInOtherThread implements Answer<Object> {

		@Override
		public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
			new Task() {

				@Override
				protected void perform() throws Throwable {
					Thread.sleep(UNIT_TIMEOUT / 5);
					invocation.invokeAndNotify();
				}
			}.executeInBackground();
			return null;
		}
	}
}