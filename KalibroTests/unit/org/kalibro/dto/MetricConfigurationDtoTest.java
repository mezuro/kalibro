package org.kalibro.dto;

import org.junit.Test;
import org.kalibro.CompoundMetric;
import org.kalibro.MetricConfiguration;
import org.kalibro.dao.BaseToolDao;
import org.kalibro.dao.RangeDao;

public class MetricConfigurationDtoTest extends AbstractDtoTest<MetricConfiguration> {

	private boolean compound;

	@Override
	protected MetricConfiguration loadFixture() {
		if (compound)
			return new MetricConfiguration(loadFixture("sc", CompoundMetric.class));
		return loadFixture("lcom4", MetricConfiguration.class);
	}

	@Override
	protected void registerLazyLoadExpectations() throws Exception {
		if (!compound) {
			doReturn("Inexistent").when(dto, "baseToolName");
			whenLazy(BaseToolDao.class, "get", "Inexistent").thenReturn(entity.getBaseTool());
		}
		whenLazy(RangeDao.class, "rangesOf", entity.getId()).thenReturn(entity.getRanges());
	}

	@Test
	public void shouldAlsoConvertCompoundMetricConfiguration() throws Exception {
		compound = true;
		setUp();
		shouldConvert();
	}
}