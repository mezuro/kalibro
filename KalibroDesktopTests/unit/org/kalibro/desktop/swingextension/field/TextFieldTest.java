package org.kalibro.desktop.swingextension.field;

import static org.junit.Assert.*;

import javax.swing.JTextPane;
import javax.swing.border.TitledBorder;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.TestCase;

public class TextFieldTest extends TestCase {

	private TextField field;
	private JTextPane textPane;

	@Before
	public void setUp() {
		field = new TextField("", 3, 10);
		textPane = (JTextPane) field.getViewport().getComponent(0);
	}

	@Test
	public void shouldNotHaveBorderByDefault() {
		assertNull(field.getBorder());
	}

	@Test
	public void shouldHaveTitledBorderWhenCreatedWithTitle() {
		field = new TextField("", 3, 10, "My title");
		assertEquals("My title", ((TitledBorder) field.getBorder()).getTitle());
	}

	@Test
	public void shoudBeEditableByDefault() {
		assertTrue(textPane.isEditable());
	}

	@Test
	public void shoudNotBeEditableWhenShowingHtml() {
		field.setShowHtml(true);
		assertFalse(textPane.isEditable());
	}

	@Test
	public void shouldTrimText() {
		field.set("\n  my text  \n");
		assertEquals("my text", field.get());
	}

	@Test
	public void shouldSetEditableOnTextPane() {
		field.setEditable(true);
		assertTrue(textPane.isEditable());

		field.setEditable(false);
		assertFalse(textPane.isEditable());
	}

	@Test
	public void shouldSetEnabledOnTextPane() {
		field.setEnabled(true);
		assertTrue(field.isEnabled());
		assertTrue(textPane.isEnabled());

		field.setEnabled(false);
		assertFalse(field.isEnabled());
		assertFalse(textPane.isEnabled());
	}
}