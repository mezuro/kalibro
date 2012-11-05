package org.kalibro.desktop.swingextension.field;

import static org.junit.Assert.assertEquals;

import java.awt.Font;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import org.kalibro.Granularity;
import org.kalibro.tests.UnitTest;

@RunWith(Theories.class)
public class ChoiceFieldTest extends UnitTest {

	private static final String NAME = "ChoiceFieldTest name";

	@DataPoints
	public static Granularity[] values() {
		return Granularity.values();
	}

	private ChoiceField<Granularity> field;

	@Before
	public void setUp() {
		field = new ChoiceField<Granularity>(NAME, values());
	}

	@Test
	public void shouldSetName() {
		assertEquals(NAME, field.getName());
	}

	@Test
	public void shouldHaveBoldFont() {
		assertEquals(Font.BOLD, field.getFont().getStyle());
	}

	@Test
	public void shouldHaveFieldSize() {
		assertEquals(new FieldSize(field), field.getSize());
	}

	@Theory
	public void shouldGet(Granularity granularity) {
		field.setSelectedItem(granularity);
		assertEquals(granularity, field.get());
	}

	@Theory
	public void shouldSet(Granularity granularity) {
		field.set(granularity);
		assertEquals(granularity, field.getSelectedItem());
	}
}