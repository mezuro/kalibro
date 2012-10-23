package org.kalibro.desktop.swingextension;

import static org.junit.Assert.assertEquals;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingConstants;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.tests.UnitTest;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

public class ButtonTest extends UnitTest {

	private ActionListener listener;

	private Button button;

	@Before
	public void setUp() {
		listener = PowerMockito.mock(ActionListener.class);
		button = new Button("", "My button", listener);
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
	public void shouldNotifyListener() {
		button.doClick();
		Mockito.verify(listener).actionPerformed(any(ActionEvent.class));
	}
}