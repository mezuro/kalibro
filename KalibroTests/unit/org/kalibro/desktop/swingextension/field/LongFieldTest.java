package org.kalibro.desktop.swingextension.field;

import static org.junit.Assert.*;

import javax.swing.JFormattedTextField;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;
import org.kalibro.desktop.ComponentFinder;
import org.kalibro.desktop.swingextension.Button;

public class LongFieldTest extends KalibroTestCase {

	private LongField field;

	@Before
	public void setUp() {
		field = new LongField("field");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldHaveSixColumns() {
		assertEquals(6, field.getColumns());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotShowFractionDigits() {
		JFormattedTextField valueField = new ComponentFinder(field).find("field", JFormattedTextField.class);
		valueField.setValue(3.1415);
		assertEquals("3", valueField.getText());
		valueField.setValue(2.7182);
		assertEquals("3", valueField.getText());
	}

	@Test
	public void shouldSetSpecialNumberWhenButtonIsClicked() {
		field = new LongField("field", Long.MIN_VALUE);
		assertNull(field.getValue());

		new ComponentFinder(field).find("field", Button.class).doClick();
		assertEquals(Long.MIN_VALUE, field.getValue().longValue());
	}
}