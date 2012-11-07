package org.kalibro.desktop.swingextension.field;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.desktop.tests.ComponentFinder;
import org.kalibro.tests.UnitTest;

public class MaybeEditableFieldTest extends UnitTest {

	private static final String NAME = "MaybeEditableFieldTest name";
	private static final String VALUE = "MaybeEditableFieldTest value";

	private MaybeEditableField<String> field;
	private ComponentFinder finder;

	@Before
	public void setUp() {
		field = new MaybeEditableField<String>(new StringField(NAME, 10));
		finder = new ComponentFinder(field);
	}

	@Test
	public void shouldSetName() {
		assertEquals(NAME, field.getName());
		field.setEditable(false);
		assertEquals(NAME, findUneditableField().getName());
	}

	@Test
	public void shouldBeEditableByDefault() {
		assertTrue(field.isEditable());
	}

	@Test
	public void shouldShowEditableFieldWhenEditable() {
		field.setEditable(true);
		assertTrue(field.isEditable());
		findEditableField();
	}

	@Test
	public void shouldShowUneditableFieldWhenNotEditable() {
		field.setEditable(false);
		assertFalse(field.isEditable());
		findUneditableField();
	}

	@Test
	public void setEditableShouldPreserveValue() {
		field.setEditable(false);
		field.set(VALUE);
		field.setEditable(true);
		assertEquals(VALUE, field.get());
	}

	@Test
	public void setUneditableShouldPreserveValue() {
		field.setEditable(true);
		findEditableField().setText(VALUE);
		field.setEditable(false);
		assertEquals(VALUE, findUneditableField().getText());
	}

	private StringField findEditableField() {
		return finder.find(NAME, StringField.class);
	}

	private UneditableField<String> findUneditableField() {
		return finder.find(NAME, UneditableField.class);
	}
}