package org.kalibro.desktop.swingextension.menu;

import java.lang.reflect.InvocationTargetException;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.tests.UnitTest;

public class ReflectionMenuItemTest extends UnitTest {

	private ReflectionMenuItem menuItem;
	private ReflectionMenuItemTest controller;

	@Before
	public void setUp() {
		controller = mock(ReflectionMenuItemTest.class);
		menuItem = new ReflectionMenuItem("", "", ' ', controller, "setUp");
	}

	@Test
	public void shouldNotAcceptInvalidMethodOnCreation() {
		assertThat(new VoidTask() {

			@Override
			protected void perform() {
				new ReflectionMenuItem("", "", ' ', controller, "invalidMethod");
			}
		}).throwsError().withMessage("ReflectionMenuItem did not found method on controller")
			.withCause(NoSuchMethodException.class);
	}

	@Test
	public void shouldInvokeControllerMethod() {
		menuItem.doClick();
		verify(controller).setUp();
	}

	@Test
	public void shouldThrowControllerException() {
		RuntimeException exception = mock(RuntimeException.class);
		doThrow(exception).when(controller).setUp();
		assertThat(click()).throwsException().withMessage("Error invoking controller method.")
			.withCause(InvocationTargetException.class);
	}

	private VoidTask click() {
		return new VoidTask() {

			@Override
			protected void perform() {
				menuItem.doClick();
			}
		};
	}
}