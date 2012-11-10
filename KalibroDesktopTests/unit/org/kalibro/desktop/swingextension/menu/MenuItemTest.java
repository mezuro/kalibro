package org.kalibro.desktop.swingextension.menu;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.desktop.swingextension.ReflectiveAction;
import org.kalibro.tests.UnitTest;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.*")
@PrepareOnlyThisForTest(MenuItem.class)
public class MenuItemTest extends UnitTest {

	private static final String NAME = "MenuItemTest name";
	private static final String TEXT = "MenuItemTest text";
	private static final char MNEMONIC = (char) new Random().nextInt();
	private static final Object TARGET = new Object();
	private static final String METHOD_NAME = "MenuItemTest method name";

	private ReflectiveAction action;
	private MenuItem menuItem;

	@Before
	public void setUp() throws Exception {
		action = mock(ReflectiveAction.class);
		whenNew(ReflectiveAction.class).withArguments(TARGET, METHOD_NAME).thenReturn(action);
		menuItem = new MenuItem(NAME, TEXT, MNEMONIC, TARGET, METHOD_NAME);
	}

	@Test
	public void shouldSetNameTextAndMnemonic() {
		assertEquals(NAME, menuItem.getName());
		assertEquals(TEXT, menuItem.getText());
		assertEquals(MNEMONIC, menuItem.getMnemonic());
	}

	@Test
	public void shouldSetReflectiveAction() {
		assertSame(action, menuItem.getAction());
	}
}