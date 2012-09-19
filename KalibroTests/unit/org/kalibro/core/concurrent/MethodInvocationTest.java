package org.kalibro.core.concurrent;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.Queue;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.TestCase;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class MethodInvocationTest extends TestCase {

	private static final String STRING = "My string";

	private Method method;
	private MethodInvocation invocation;

	@Before
	public void setUp() throws Exception {
		method = String.class.getMethod("substring", int.class);
	}

	@Test
	public void shouldInvoke() throws Throwable {
		createInvocation(3);
		invocation.invoke();
		assertEquals("string", invocation.getResult());
	}

	@Test
	public void shouldRetrieveIfInvocationWasDone() {
		createInvocation(4);
		assertFalse(invocation.done());
		invocation.invoke();
		assertTrue(invocation.done());
	}

	@Test
	public void shouldThrowExceptionWhenRetrievingResult() {
		createInvocation(null);
		invocation.invoke();
		assertThat(getResult()).doThrow(IllegalArgumentException.class);
	}

	@Test
	public void shouldThrowCauseOfInvocationTargetException() {
		createInvocation(-1);
		invocation.invoke();
		assertThat(getResult()).doThrow(StringIndexOutOfBoundsException.class);
	}

	private VoidTask getResult() {
		return new VoidTask() {

			@Override
			public void perform() throws Throwable {
				invocation.getResult();
			}
		};
	}

	@Test
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
			new VoidTask() {

				@Override
				public void perform() throws InterruptedException {
					Thread.sleep(200);
					invocation.invokeAndNotify();
				}
			}.executeInBackground();
			return null;
		}
	}
}