package org.kalibro.desktop.swingextension;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingConstants;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.TestCase;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

public class ButtonTest extends TestCase {

	private ActionListener listener;

	private Button button;

	@Before
	public void setUp() {
		listener = PowerMockito.mock(ActionListener.class);
		button = new Button("", "My button", listener);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldHaveBoldFont() {
		assertEquals(Font.BOLD, button.getFont().getStyle());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldHaveCenterHorizontalAlignment() {
		assertEquals(SwingConstants.CENTER, button.getHorizontalAlignment());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldHaveCenterVerticalAlignment() {
		assertEquals(SwingConstants.CENTER, button.getVerticalAlignment());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotifyListener() {
		button.doClick();
		Mockito.verify(listener).actionPerformed(any(ActionEvent.class));
	}
}