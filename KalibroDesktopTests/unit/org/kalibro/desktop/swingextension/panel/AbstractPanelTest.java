package org.kalibro.desktop.swingextension.panel;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.TestCase;

public class AbstractPanelTest extends TestCase {

	private AbstractPanel<?> panel;

	@Before
	public void setUp() {
		panel = new LanguagePanelStub();
	}

	@Test
	public void shouldSetMinimumSize() {
		assertTrue(panel.isMinimumSizeSet());
	}

	@Test
	public void setWidthShouldNotChangeHeight() {
		int height = panel.getSize().height;
		panel.setWidth(42);
		assertEquals(42, panel.getSize().width);
		assertEquals(height, panel.getSize().height);
	}
}