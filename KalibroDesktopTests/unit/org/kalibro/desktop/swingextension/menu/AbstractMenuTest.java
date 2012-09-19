package org.kalibro.desktop.swingextension.menu;

import static org.junit.Assert.*;

import java.awt.Component;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.TestCase;

public class AbstractMenuTest extends TestCase {

	private AbstractMenu menu;

	private boolean itemsCreated, menuBuilded;

	@Before
	public void setUp() {
		itemsCreated = false;
		menuBuilded = false;
		menu = new MenuStub();
	}

	@Test
	public void shouldNameTextAndMnemonic() {
		assertEquals("menu", menu.getName());
		assertEquals("Menu", menu.getText());
		assertEquals('M', menu.getMnemonic());
	}

	@Test
	public void shouldCreateItemsAndBuildMenu() {
		assertTrue(itemsCreated);
		assertTrue(menuBuilded);
	}

	@Test
	public void shouldListenToItself() {
		assertSame(menu, menu.getMenuListeners()[0]);
		menu.menuSelected(null);
		menu.menuDeselected(null);
		menu.menuCanceled(null);
	}

	private class MenuStub extends AbstractMenu {

		public MenuStub() {
			super("menu", "Menu", 'M');
		}

		@Override
		protected void createItems(Component... innerComponents) {
			itemsCreated = true;
		}

		@Override
		protected void buildMenu() {
			menuBuilded = true;
		}
	}
}