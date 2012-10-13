package org.kalibro.dto;

import org.junit.Test;
import org.kalibro.MetricConfiguration;
import org.kalibro.MetricResult;

public class MetricResultDtoTest extends AbstractDtoTest<MetricResult> {

	@Override
	protected MetricResult loadFixture() {
		return new MetricResult(new MetricConfiguration(), new Throwable());
	}

	@Test
	public void shouldAlsoConvertNormalResult() throws Exception {
		entity = new MetricResult(entity.getConfiguration(), Double.POSITIVE_INFINITY);
		entity.addDescendentResult(42.0);
		createDto();
		shouldConvert();
	}
}