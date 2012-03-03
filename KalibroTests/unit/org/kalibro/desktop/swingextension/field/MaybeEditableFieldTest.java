package org.kalibro.desktop.swingextension.field;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;
import org.kalibro.desktop.ComponentFinder;

public class MaybeEditableFieldTest extends KalibroTestCase {

	private MaybeEditableField<String> field;

	private ComponentFinder finder;

	@Before
	public void setUp() {
		field = new MaybeEditableField<String>(new StringField("field", 10));
		finder = new ComponentFinder(field);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldBeEditableByDefault() {
		assertTrue(field.isEditable());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldShowEditableFieldWhenSettingEditable() {
		field.setEditable(true);
		assertTrue(field.isEditable());
		findEditableField();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldShowUneditableFieldWhenSettingNotEditable() {
		field.setEditable(false);
		assertFalse(field.isEditable());
		findUneditableField();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldShowLastValueSet() {
		testLastValue("First value", false);
		testLastValue("Another value", true);
	}

	private void testLastValue(String value, boolean editable) {
		field.set(value);
		field.setEditable(editable);
		Field<String> innerField = editable ? findEditableField() : findUneditableField();
		assertEquals(value, field.get());
		assertEquals(value, innerField.get());
	}

	private StringField findEditableField() {
		return finder.find("field", StringField.class);
	}

	private UneditableField<String> findUneditableField() {
		return finder.find("field", UneditableField.class);
	}
}