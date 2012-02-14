package org.kalibro.desktop.swingextension.field;

import static org.junit.Assert.*;

import javax.swing.JTextPane;
import javax.swing.border.TitledBorder;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;

public class TextFieldTest extends KalibroTestCase {

	private TextField field;
	private JTextPane textPane;

	@Before
	public void setUp() {
		field = new TextField("", 3, 10);
		textPane = (JTextPane) field.getViewport().getComponent(0);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotHaveBorderByDefault() {
		assertNull(field.getBorder());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldHaveTitledBorderWhenCreatedWithTitle() {
		field = new TextField("", 3, 10, "My title");
		assertEquals("My title", ((TitledBorder) field.getBorder()).getTitle());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shoudBeEditableByDefault() {
		assertTrue(textPane.isEditable());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shoudNotBeEditableWhenShowingHtml() {
		field.setShowHtml(true);
		assertFalse(textPane.isEditable());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldTrimText() {
		field.setValue("\n  my text  \n");
		assertEquals("my text", field.getValue());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testEditable() {
		assertTrue(textPane.isEditable());
		field.setEditable(false);
		assertFalse(textPane.isEditable());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testEnabled() {
		assertTrue(textPane.isEnabled());
		field.setEnabled(false);
		assertFalse(field.isEnabled());
		assertFalse(textPane.isEnabled());
	}
}