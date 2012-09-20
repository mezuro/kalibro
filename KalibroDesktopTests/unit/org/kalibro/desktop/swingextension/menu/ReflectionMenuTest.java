package org.kalibro.desktop.swingextension.menu;

import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.TestCase;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.desktop.CrudController;
import org.kalibro.desktop.configuration.ConfigurationController;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.reflect.Whitebox;

public class ReflectionMenuTest extends TestCase {

	private ReflectionMenuItem menuItem;
	private ConfigurationController controller;

	@Before
	public void setUp() {
		controller = PowerMockito.mock(ConfigurationController.class);
		menuItem = new ReflectionMenuItem("", "", ' ', controller, "open");
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
		Mockito.verify(controller).open();
	}

	@Test
	public void shouldThrowControllerException() {
		PowerMockito.doThrow(new IllegalArgumentException()).when(controller).open();
		assertThat(click()).doThrow(IllegalArgumentException.class);
	}

	@Test
	public void shouldThrowErrorOnBizarreAccessException() throws Exception {
		Method method = CrudController.class.getDeclaredMethod("unmodified");
		Whitebox.setInternalState(menuItem, "method", method);
		assertThat(click()).throwsError().withMessage("Could not access controller method")
			.withCause(IllegalAccessException.class);
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