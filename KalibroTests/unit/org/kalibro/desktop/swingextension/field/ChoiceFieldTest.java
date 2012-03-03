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
	public void shouldGetValue() {
		for (Granularity granularity : Granularity.values()) {
			field.setSelectedItem(granularity);
			assertEquals(granularity, field.get());
		}
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void setValueShouldChangeSelectedItem() {
		for (Granularity granularity : Granularity.values()) {
			field.set(granularity);
			assertEquals(granularity, field.getSelectedItem());
		}
	}
}