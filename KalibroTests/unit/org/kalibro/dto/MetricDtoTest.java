package org.kalibro.dto;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.kalibro.CompoundMetric;
import org.kalibro.Metric;
import org.kalibro.NativeMetric;

public class MetricDtoTest extends AbstractDtoTest<Metric> {

	@Override
	protected Metric loadFixture() {
		return loadFixture("lcom4", NativeMetric.class);
	}

	@Test
	public void shouldAlsoConvertCompoundMetric() throws Exception {
		entity = loadFixture("sc", CompoundMetric.class);
		createDto();
		shouldConvert();
	}

	@Test
	public void shouldConvertNullDescriptionIntoEmptyString() throws Exception {
		when(dto, "description").thenReturn(null);
		assertEquals("", dto.convert().getDescription());
	}
}