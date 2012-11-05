package org.kalibro.desktop.swingextension.menu;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.util.Random;

import org.junit.Test;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.tests.ThrowableMatcher;
import org.kalibro.tests.UnitTest;

public class ReflectionMenuItemTest extends UnitTest {

	private static final String NAME = "ReflectionMenuItemTest name";
	private static final String TEXT = "ReflectionMenuItemTest text";
	private static final char MNEMONIC = (char) new Random().nextInt();

	private static boolean invoked;

	public static void sampleStaticMethod() {
		invoked = true;
	}

	private ReflectionMenuItem menuItem;

	@Test
	public void shouldNotCreateWithInvalidMethod() {
		assertConstructionThrowsError(null, "invalidMethod").withCause(NullPointerException.class);
		assertConstructionThrowsError(this, "invalidMethod").withCause(NoSuchMethodException.class);
		assertConstructionThrowsError(Object.class, "invalidStaticMethod").withCause(NoSuchMethodException.class);
	}

	private ThrowableMatcher assertConstructionThrowsError(final Object target, final String methodName) {
		return assertThat(new VoidTask() {

			@Override
			protected void perform() throws Throwable {
				createMenuItem(target, methodName);
			}
		}).throwsError().withMessage("ReflectionMenuItem did not found method on target.");
	}

	@Test
	public void shouldInvokeInstanceMethodForObjectTarget() {
		ReflectionMenuItemTest target = mock(ReflectionMenuItemTest.class);
		createMenuItem(target, "shouldInvokeInstanceMethodForObjectTarget");

		menuItem.doClick();
		verify(target).shouldInvokeInstanceMethodForObjectTarget();
	}

	@Test
	public void shouldInvokeStaticMethodForClassTarget() {
		createMenuItem(ReflectionMenuItemTest.class, "sampleStaticMethod");

		assertFalse(invoked);
		menuItem.doClick();
		assertTrue(invoked);
	}

	@Test
	public void shouldThrowExceptionIfTargetThrowsException() {
		ReflectionMenuItemTest target = mock(ReflectionMenuItemTest.class);
		createMenuItem(target, "shouldThrowExceptionIfTargetThrowsException");
		doThrow(new RuntimeException()).when(target).shouldThrowExceptionIfTargetThrowsException();

		assertClickThrowsException().withCause(InvocationTargetException.class);
	}

	private void createMenuItem(Object target, String methodName) {
		menuItem = new ReflectionMenuItem(NAME, TEXT, MNEMONIC, target, methodName);
	}

	private ThrowableMatcher assertClickThrowsException() {
		return assertThat(new VoidTask() {

			@Override
			protected void perform() {
				menuItem.doClick();
			}
		}).throwsException().withMessage("Error invoking target method.");
	}
}