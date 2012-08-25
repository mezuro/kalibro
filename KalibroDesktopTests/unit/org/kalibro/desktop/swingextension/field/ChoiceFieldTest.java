package org.kalibro.desktop.swingextension.field;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.TestCase;
import org.kalibro.core.model.enums.Granularity;

public class ChoiceFieldTest extends TestCase {

	private ChoiceField<Granularity> field;

	@Before
	public void setUp() {
		field = new ChoiceField<Granularity>("", Granularity.values());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldGet() {
		for (Granularity granularity : Granularity.values()) {
			field.setSelectedItem(granularity);
			assertEquals(granularity, field.get());
		}
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSet() {
		for (Granularity granularity : Granularity.values()) {
			field.set(granularity);
			assertEquals(granularity, field.getSelectedItem());
		}
	}
}