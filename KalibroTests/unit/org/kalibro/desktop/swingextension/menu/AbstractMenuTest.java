package org.kalibro.desktop.swingextension.menu;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;

public class AbstractMenuTest extends KalibroTestCase {

	private AbstractMenu menu;

	private boolean itemsCreated, menuBuilded;

	@Before
	public void setUp() {
		itemsCreated = false;
		menuBuilded = false;
		menu = new MenuStub();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNameTextAndMnemonic() {
		assertEquals("menu", menu.getName());
		assertEquals("Menu", menu.getText());
		assertEquals('M', menu.getMnemonic());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shoudCreateItemsAndBuildMenu() {
		assertTrue(itemsCreated);
		assertTrue(menuBuilded);
	}

	private class MenuStub extends AbstractMenu {

		public MenuStub() {
			super("menu", "Menu", 'M');
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