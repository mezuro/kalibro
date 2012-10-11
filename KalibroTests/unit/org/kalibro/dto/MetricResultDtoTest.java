package org.kalibro.dto;

import java.util.Random;

import org.junit.Test;
import org.kalibro.MetricConfiguration;
import org.kalibro.MetricResult;

public class MetricResultDtoTest extends AbstractDtoTest<MetricResult> {

	@Override
	protected MetricResult loadFixture() {
		MetricConfiguration configuration = loadFixture("lcom4", MetricConfiguration.class);
		MetricResult metricResult = new MetricResult(configuration, new Random().nextDouble());
		metricResult.addDescendentResult(42.0);
		return metricResult;
	}

	@Test
	public void shouldAlsoConvertErrorResult() throws Exception {
		entity = new MetricResult(entity.getConfiguration(), new Exception());
		createDto();
		shouldConvert();
	}
}