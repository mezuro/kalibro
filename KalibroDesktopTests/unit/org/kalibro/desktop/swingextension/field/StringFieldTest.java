package org.kalibro.desktop.swingextension.field;

import static org.junit.Assert.assertEquals;

import java.awt.Font;

import javax.swing.SwingConstants;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.tests.UnitTest;

public class StringFieldTest extends UnitTest {

	private static final int COLUMNS = 5;
	private static final String NAME = "StringFieldTest name";
	private static final String TEXT = "\n StringFieldTest text \n";

	private StringField field;

	@Before
	public void setUp() {
		field = new StringField(NAME, COLUMNS);
	}

	@Test
	public void shouldSetNameAndColumns() {
		assertEquals(NAME, field.getName());
		assertEquals(COLUMNS, field.getColumns());
	}

	@Test
	public void shouldHavePlainFont() {
		assertEquals(Font.PLAIN, field.getFont().getStyle());
	}

	@Test
	public void shouldHaveLeftHorizontalAlignment() {
		assertEquals(SwingConstants.LEFT, field.getHorizontalAlignment());
	}

	@Test
	public void shouldHaveFieldSize() {
		assertEquals(new FieldSize(field), field.getSize());
	}

	@Test
	public void shouldTrimText() {
		assertDifferent(TEXT, TEXT.trim());
		field.set(TEXT);
		assertEquals(TEXT.trim(), field.get());
	}
}