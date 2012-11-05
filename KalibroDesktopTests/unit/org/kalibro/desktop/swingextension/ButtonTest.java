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
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

public class ButtonTest extends UnitTest {

	private static final String NAME = "ButtonTest name";
	private static final String TEXT = "ButtonTest text";

	private ActionListener listener;

	private Button button;

	@Before
	public void setUp() {
		listener = PowerMockito.mock(ActionListener.class);
		button = new Button(NAME, TEXT, listener);
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
	public void shouldHaveCenterHorizontalAlignment() {
		assertEquals(SwingConstants.CENTER, button.getHorizontalAlignment());
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
	public void shouldNotifyListener() {
		button.doClick();
		Mockito.verify(listener).actionPerformed(any(ActionEvent.class));
	}
}