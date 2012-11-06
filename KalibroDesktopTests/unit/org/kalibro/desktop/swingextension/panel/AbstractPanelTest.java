package org.kalibro.desktop.swingextension.panel;

import static org.junit.Assert.*;

import java.awt.Dimension;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.Language;
import org.kalibro.desktop.swingextension.field.ChoiceField;
import org.kalibro.tests.UnitTest;

public class AbstractPanelTest extends UnitTest {

	private AbstractPanel<Language> panel;

	@Before
	public void setUp() {
		panel = new LanguagePanel();
	}

	@Test
	public void shouldSetName() {
		assertEquals("language", panel.getName());
	}

	@Test
	public void shouldCreateComponentsAndBuildPanel() {
		assertClassEquals(ChoiceField.class, panel.getComponent(0));
	}

	@Test
	public void shouldAdjustSize() {
		assertTrue(panel.isMinimumSizeSet());
		assertEquals(panel.getPreferredSize(), panel.getMinimumSize());
		assertEquals(panel.getPreferredSize(), panel.getSize());
	}

	@Test
	public void shouldSetWidth() {
		int newWidth = 42, oldHeight = panel.getSize().height;
		panel.setWidth(newWidth);
		Dimension newDimension = new Dimension(newWidth, oldHeight);

		assertEquals(newDimension, panel.getSize());
		assertEquals(newDimension, panel.getMinimumSize());
		assertEquals(newDimension, panel.getPreferredSize());
	}

	@Test
	public void shouldGetValue() {
		assertEquals(Language.values()[0], panel.get());
	}
}