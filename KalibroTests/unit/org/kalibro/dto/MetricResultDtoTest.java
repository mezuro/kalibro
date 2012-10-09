package org.kalibro.dto;

import java.util.Random;

import org.junit.Test;
import org.kalibro.CompoundMetric;
import org.kalibro.MetricConfiguration;
import org.kalibro.MetricResult;

public class MetricResultDtoTest extends AbstractDtoTest<MetricResult> {

	@Override
	protected MetricResult loadFixture() {
		MetricConfiguration configuration = loadFixture("lcom4", MetricConfiguration.class);
		return new MetricResult(configuration, new Random().nextDouble());
	}

	@Test
	public void shouldAlsoConvertErrorResult() throws Exception {
		CompoundMetric metric = loadFixture("sc", CompoundMetric.class);
		entity = new MetricResult(metric, new Exception());
		createDto();
		when(dto, "configuration").thenReturn(new MetricConfiguration(metric));
		shouldConvert();
	}
}