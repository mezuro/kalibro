package org.kalibro.desktop.swingextension.menu;

import static org.junit.Assert.*;

import java.awt.Component;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.tests.UnitTest;

public class AbstractMenuTest extends UnitTest {

	private static final String NAME = "AbstractMenuTest name";
	private static final String TEXT = "AbstractMenuTest text";
	private static final char MNEMONIC = (char) new Random().nextInt();
	private static final Component INNER_COMPONENT = mock(Component.class);

	private boolean menuBuilded;
	private Component[] itemsCreated;

	private AbstractMenu menu;

	@Before
	public void setUp() {
		menu = new MenuMock();
	}

	@Test
	public void shouldSetNameTextAndMnemonic() {
		assertEquals(NAME, menu.getName());
		assertEquals(TEXT, menu.getText());
		assertEquals(MNEMONIC, menu.getMnemonic());
	}

	@Test
	public void shouldCreateItemsAndBuildMenu() {
		assertArrayEquals(array(INNER_COMPONENT), itemsCreated);
		assertTrue(menuBuilded);
	}

	@Test
	public void shouldHaveDefaultConstructor() throws Exception {
		menu = mockAbstract(AbstractMenu.class);
	}

	private class MenuMock extends AbstractMenu {

		public MenuMock() {
			super(NAME, TEXT, MNEMONIC, INNER_COMPONENT);
		}

		@Override
		protected void createItems(Component... innerComponents) {
			itemsCreated = innerComponents;
		}

		@Override
		protected void buildMenu() {
			menuBuilded = true;
		}
	}
}