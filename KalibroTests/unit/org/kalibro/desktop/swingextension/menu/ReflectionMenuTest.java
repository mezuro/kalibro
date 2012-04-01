package org.kalibro.desktop.swingextension.menu;

import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.concurrent.Task;
import org.kalibro.desktop.configuration.ConfigurationController;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.reflect.Whitebox;

public class ReflectionMenuTest extends KalibroTestCase {

	private ReflectionMenuItem menuItem;
	private ConfigurationController controller;

	@Before
	public void setUp() {
		controller = PowerMockito.mock(ConfigurationController.class);
		menuItem = new ReflectionMenuItem("", "", ' ', controller, "open");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotAcceptInvalidMethodOnCreation() {
		checkKalibroError(new Task() {

			@Override
			protected void perform() throws Throwable {
				new ReflectionMenuItem("", "", ' ', controller, "invalidMethod");
			}
		}, "ReflectionMenuItem did not found method on controller", NoSuchMethodException.class);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldInvokeControllerMethod() {
		menuItem.doClick();
		Mockito.verify(controller).open();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldThrowControllerException() {
		PowerMockito.doThrow(new IllegalArgumentException()).when(controller).open();
		checkException(new Task() {

			@Override
			protected void perform() throws Throwable {
				menuItem.doClick();
			}
		}, IllegalArgumentException.class);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldThrowErrorOnBizarreAccessException() throws Exception {
		Method method = ConfigurationController.class.getDeclaredMethod("unmodified");
		Whitebox.setInternalState(menuItem, "method", method);
		checkKalibroError(new Task() {

			@Override
			protected void perform() throws Throwable {
				menuItem.doClick();
			}
		}, "Could not access controller method", IllegalAccessException.class);
	}
}