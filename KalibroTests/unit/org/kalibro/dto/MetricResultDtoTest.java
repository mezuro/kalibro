package org.kalibro.dto;

import org.junit.Test;
import org.kalibro.MetricConfiguration;
import org.kalibro.MetricResult;

public class MetricResultDtoTest extends AbstractDtoTest<MetricResult> {

	private boolean normal;

	@Override
	protected MetricResult loadFixture() {
		if (normal)
			return new MetricResult(entity.getConfiguration(), Double.POSITIVE_INFINITY);
		return new MetricResult(new MetricConfiguration(), new Throwable());
	}

	@Test
	public void shouldAlsoConvertNormalResult() throws Exception {
		normal = true;
		setUp();
		shouldConvert();
	}
}