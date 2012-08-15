package org.kalibro.desktop.swingextension;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingConstants;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

public class RadioButtonTest extends KalibroTestCase {

	private ActionListener listener;

	private RadioButton button;

	@Before
	public void setUp() {
		listener = PowerMockito.mock(ActionListener.class);
		button = new RadioButton("", "My button", listener);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldHaveBoldFont() {
		assertEquals(Font.BOLD, button.getFont().getStyle());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldHaveLeftHorizontalAlignment() {
		assertEquals(SwingConstants.LEFT, button.getHorizontalAlignment());
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