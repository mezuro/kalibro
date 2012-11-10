package org.kalibro.desktop.swingextension;

import static org.junit.Assert.*;

import org.junit.Test;
import org.kalibro.core.concurrent.TaskMatcher;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.tests.UnitTest;

public class ReflectiveActionTest extends UnitTest {

	private static boolean invoked;

	public static void sampleStaticMethod() {
		invoked = true;
	}

	private ReflectiveAction action;

	@Test
	public void shouldNotCreateWithInvalidMethod() {
		assertNewWith(null, "invalidMethod").doThrow(NullPointerException.class);
		assertNewWith(this, "invalidMethod").throwsError()
			.withMessage("Error finding method: org.kalibro.desktop.swingextension.ReflectiveActionTest.invalidMethod");
		assertNewWith(Object.class, "invalidStaticMethod").throwsError()
			.withMessage("Error finding method: java.lang.Object.invalidStaticMethod");
	}

	private TaskMatcher assertNewWith(final Object target, final String methodName, final Object... arguments) {
		return assertThat(new VoidTask() {

			@Override
			protected void perform() throws Throwable {
				createAction(target, methodName, arguments);
			}
		});
	}

	@Test
	public void shouldInvokeInstanceMethodForObjectTarget() {
		ReflectiveActionTest target = mock(ReflectiveActionTest.class);
		createAction(target, "shouldInvokeInstanceMethodForObjectTarget");

		action.actionPerformed(null);
		verify(target).shouldInvokeInstanceMethodForObjectTarget();
	}

	@Test
	public void shouldInvokeStaticMethodForClassTarget() {
		createAction(ReflectiveActionTest.class, "sampleStaticMethod");

		assertFalse(invoked);
		action.actionPerformed(null);
		assertTrue(invoked);
	}

	@Test
	public void shouldThrowExceptionIfTargetThrowsException() {
		RuntimeException exception = new RuntimeException();
		ReflectiveActionTest target = mock(ReflectiveActionTest.class);
		createAction(target, "shouldThrowExceptionIfTargetThrowsException");
		doThrow(exception).when(target).shouldThrowExceptionIfTargetThrowsException();

		assertPerformThrows(exception);
	}

	private void createAction(Object target, String methodName, Object... arguments) {
		action = new ReflectiveAction(target, methodName, arguments);
	}

	private void assertPerformThrows(Throwable toBeThrown) {
		assertThat(new VoidTask() {

			@Override
			protected void perform() {
				action.actionPerformed(null);
			}
		}).doThrow(toBeThrown);
	}
}