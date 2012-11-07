package org.kalibro.desktop.swingextension.field;

import static org.junit.Assert.*;

import javax.swing.JTextPane;
import javax.swing.border.TitledBorder;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.tests.UnitTest;

public class TextFieldTest extends UnitTest {

	private static final int LINES = 3;
	private static final int COLUMNS = 10;
	private static final String NAME = "TextFieldTest name";
	private static final String TITLE = "TextFieldTest title";
	private static final String TEXT = "\n TextFieldTest text \n";

	private TextField field;
	private JTextPane textPane;

	@Before
	public void setUp() {
		field = new TextField(NAME, LINES, COLUMNS);
		textPane = (JTextPane) field.getViewport().getComponent(0);
	}

	@Test
	public void shouldNotHaveTitleByDefault() {
		assertFalse(field.getBorder() instanceof TitledBorder);
	}

	@Test
	public void shouldAddTitle() {
		field.titled(TITLE);
		assertEquals(TITLE, ((TitledBorder) field.getBorder()).getTitle());
	}

	@Test
	public void shoudBeEditableByDefault() {
		assertTrue(textPane.isEditable());
	}

	@Test
	public void shoudNotBeEditableWhenShowingHtml() {
		field.showingHtml();
		assertFalse(textPane.isEditable());
	}

	@Test
	public void shouldTrimText() {
		assertDifferent(TEXT, TEXT.trim());
		field.set(TEXT);
		assertEquals(TEXT.trim(), field.get());
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