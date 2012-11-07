package org.kalibro.dto;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.kalibro.CompoundMetric;
import org.kalibro.Metric;
import org.kalibro.NativeMetric;

public class MetricDtoTest extends AbstractDtoTest<Metric> {

	private boolean compound;

	@Override
	protected Metric loadFixture() {
		if (compound)
			return loadFixture("sc", CompoundMetric.class);
		return loadFixture("lcom4", NativeMetric.class);
	}

	@Test
	public void shouldAlsoConvertCompoundMetric() throws Exception {
		compound = true;
		setUp();
		shouldConvert();
	}

	@Test
	public void shouldConvertNullDescriptionIntoEmptyString() throws Exception {
		when(dto, "description").thenReturn(null);
		assertEquals("", dto.convert().getDescription());
	}
}