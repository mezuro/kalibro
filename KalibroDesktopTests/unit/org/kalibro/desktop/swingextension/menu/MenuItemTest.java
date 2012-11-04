package org.kalibro.desktop.swingextension.menu;

import static org.junit.Assert.assertEquals;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.tests.UnitTest;

public class MenuItemTest extends UnitTest {

	private static final String NAME = "MenuItemTest name";
	private static final String TEXT = "MenuItemTest text";
	private static final char MNEMONIC = (char) new Random().nextInt();

	private ActionListener listener;

	private MenuItem item;

	@Before
	public void setUp() {
		listener = mock(ActionListener.class);
		item = new MenuItem(NAME, TEXT, MNEMONIC, listener);
	}

	@Test
	public void shouldSetNameTextAndMnemonic() {
		assertEquals(NAME, item.getName());
		assertEquals(TEXT, item.getText());
		assertEquals(MNEMONIC, item.getMnemonic());
	}

	@Test
	public void shouldNotifyListeners() {
		item.doClick();
		verify(listener).actionPerformed(any(ActionEvent.class));
	}
}