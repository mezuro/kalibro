package org.kalibro.desktop.swingextension.field;

import static org.junit.Assert.*;

import java.awt.Font;
import java.util.Random;

import javax.swing.SwingConstants;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.tests.UnitTest;

public class UneditableFieldTest extends UnitTest {

	private static final String NAME = "UneditableFieldTest name";
	private static final Integer VALUE = new Random().nextInt();

	private UneditableField<Integer> field;

	@Before
	public void setUp() {
		field = new UneditableField<Integer>(NAME);
	}

	@Test
	public void shouldSetName() {
		assertEquals(NAME, field.getName());
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
	public void shouldHaveCenterVerticalAlignment() {
		assertEquals(SwingConstants.CENTER, field.getVerticalAlignment());
	}

	@Test
	public void shouldShowValue() {
		field.set(VALUE);
		assertEquals(VALUE.toString(), field.getText());

		field.set(null);
		assertEquals("", field.getText());
	}

	@Test
	public void shouldRetrieveValue() {
		field.set(VALUE);
		assertEquals(VALUE, field.get());
	}

	@Test
	public void shouldChangeWidthWhenSettingValue() {
		int emptyWidth = (int) field.getSize().getWidth();

		field.set(123456789);
		int nineWidth = (int) field.getSize().getWidth();
		assertTrue(nineWidth > emptyWidth);

		field.set(1234);
		int fourWidth = (int) field.getSize().getWidth();
		assertTrue(fourWidth < nineWidth);
	}
}