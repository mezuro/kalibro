package org.kalibro.desktop.swingextension;

import static org.junit.Assert.assertEquals;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingConstants;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.desktop.swingextension.field.FieldSize;
import org.kalibro.tests.UnitTest;
import org.powermock.api.mockito.PowerMockito;

public class RadioButtonTest extends UnitTest {

	private static final String NAME = "RadioButtonTest name";
	private static final String TEXT = "RadioButtonTest text";

	private ActionListener listener;
	private RadioButton button;

	@Before
	public void setUp() {
		listener = PowerMockito.mock(ActionListener.class);
		button = new RadioButton(NAME, TEXT, listener);
	}

	@Test
	public void shouldSetNameAndText() {
		assertEquals(NAME, button.getName());
		assertEquals(TEXT, button.getText());
	}

	@Test
	public void shouldHaveBoldFont() {
		assertEquals(Font.BOLD, button.getFont().getStyle());
	}

	@Test
	public void shouldHaveLeftHorizontalAlignment() {
		assertEquals(SwingConstants.LEFT, button.getHorizontalAlignment());
	}

	@Test
	public void shouldHaveCenterVerticalAlignment() {
		assertEquals(SwingConstants.CENTER, button.getVerticalAlignment());
	}

	@Test
	public void shouldHaveFieldSize() {
		assertEquals(new FieldSize(button), button.getSize());
	}

	@Test
	public void shouldNotifyListeners() {
		button.doClick();
		verify(listener).actionPerformed(any(ActionEvent.class));
	}
}