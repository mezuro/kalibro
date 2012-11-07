package org.kalibro.dto;

import org.junit.Test;
import org.kalibro.MetricConfiguration;
import org.kalibro.MetricResult;
import org.kalibro.dao.MetricResultDao;

public class MetricResultDtoTest extends AbstractDtoTest<MetricResult> {

	private boolean normal;

	@Override
	protected MetricResult loadFixture() {
		if (normal)
			return new MetricResult(entity.getConfiguration(), Double.POSITIVE_INFINITY);
		return new MetricResult(new MetricConfiguration(), new Throwable());
	}

	@Override
	protected void registerLazyLoadExpectations() throws Exception {
		whenLazy(MetricResultDao.class, "descendantResultsOf", entity.getId())
			.thenReturn(entity.getDescendantResults());
	}

	@Test
	public void shouldAlsoConvertNormalResult() throws Exception {
		normal = true;
		setUp();
		shouldConvert();
	}
}