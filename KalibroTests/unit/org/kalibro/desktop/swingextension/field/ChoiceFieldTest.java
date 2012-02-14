package org.kalibro.desktop.swingextension.field;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.model.enums.Granularity;

public class ChoiceFieldTest extends KalibroTestCase {

	private ChoiceField<Granularity> field;

	@Before
	public void setUp() {
		field = new ChoiceField<Granularity>("", Granularity.values());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void getValueShouldReturnSelectedItem() {
		field.setSelectedItem(Granularity.METHOD);
		assertEquals(Granularity.METHOD, field.getValue());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void setValueShouldChangeSelectedItem() {
		field.setValue(Granularity.PACKAGE);
		assertEquals(Granularity.PACKAGE, field.getSelectedItem());
	}
}