package org.kalibro.desktop.swingextension.menu;

import static org.junit.Assert.assertEquals;

import java.util.Random;

import javax.swing.event.MenuEvent;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.tests.UnitTest;

public class AbstractMenuTest extends UnitTest {

	private static final String NAME = "AbstractMenuTest name";
	private static final String TEXT = "AbstractMenuTest text";
	private static final char MNEMONIC = (char) new Random().nextInt();

	private AbstractMenu menu;

	@Before
	public void setUp() {
		menu = new AbstractMenu() { /* stub */};
	}

	@Test
	public void checkDefaultConstruction() {
		assertEquals("", menu.getName());
		assertEquals("", menu.getText());
		assertEquals(' ', menu.getMnemonic());
	}

	@Test
	public void shouldSetNameTextAndMnemonic() {
		menu = new AbstractMenu(NAME, TEXT, MNEMONIC) { /* stub */};
		assertEquals(NAME, menu.getName());
		assertEquals(TEXT, menu.getText());
		assertEquals(MNEMONIC, menu.getMnemonic());
	}

	@Test
	public void shouldListenToItself() {
		assertDeepEquals(array(menu), menu.getMenuListeners());
	}

	@Test
	public void shouldAdaptMenuListener() {
		MenuEvent event = mock(MenuEvent.class);
		menu.menuSelected(event);
		menu.menuDeselected(event);
		menu.menuCanceled(event);
		verifyZeroInteractions(event);
	}
}