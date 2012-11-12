package org.kalibro.desktop.swingextension.menu;

import static org.junit.Assert.*;

import java.util.Random;

import javax.swing.event.MenuEvent;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.tests.UnitTest;

public class AbstractMenuTest extends UnitTest {

	private static final String NAME = "AbstractMenuTest name";
	private static final String TEXT = "AbstractMenuTest text";
	private static final char MNEMONIC = (char) new Random().nextInt();

	private boolean menuBuilded, itemsCreated;

	private AbstractMenu menu;

	@Before
	public void setUp() {
		menuBuilded = false;
		itemsCreated = false;
		menu = new MenuMock(NAME, TEXT, MNEMONIC);
	}

	@Test
	public void checkDefaultConstruction() {
		menu = new MenuMock();
		assertEquals("", menu.getName());
		assertEquals("", menu.getText());
		assertEquals(' ', menu.getMnemonic());
	}

	@Test
	public void shouldSetNameTextAndMnemonic() {
		assertEquals(NAME, menu.getName());
		assertEquals(TEXT, menu.getText());
		assertEquals(MNEMONIC, menu.getMnemonic());
	}

	@Test
	public void shouldCreateItemsAndBuildMenu() {
		assertTrue(itemsCreated);
		assertTrue(menuBuilded);
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

	private class MenuMock extends AbstractMenu {

		public MenuMock() {
			super();
		}

		public MenuMock(String name, String text, char mnemonic) {
			super(name, text, mnemonic);
		}

		@Override
		protected void createItems() {
			itemsCreated = true;
		}

		@Override
		protected void buildMenu() {
			menuBuilded = true;
		}
	}
}