package org.kalibro.desktop.swingextension.field;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.Granularity;
import org.kalibro.tests.UnitTest;

public class ChoiceFieldTest extends UnitTest {

	private ChoiceField<Granularity> field;

	@Before
	public void setUp() {
		field = new ChoiceField<Granularity>("", Granularity.values());
	}

	@Test
	public void shouldGet() {
		for (Granularity granularity : Granularity.values()) {
			field.setSelectedItem(granularity);
			assertEquals(granularity, field.get());
		}
	}

	@Test
	public void shouldSet() {
		for (Granularity granularity : Granularity.values()) {
			field.set(granularity);
			assertEquals(granularity, field.getSelectedItem());
		}
	}
}