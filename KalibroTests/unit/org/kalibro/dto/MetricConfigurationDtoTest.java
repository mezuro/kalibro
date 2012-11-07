package org.kalibro.dto;

import static org.junit.Assert.assertNull;

import java.util.Random;

import org.junit.Test;
import org.kalibro.CompoundMetric;
import org.kalibro.MetricConfiguration;
import org.kalibro.dao.BaseToolDao;
import org.kalibro.dao.RangeDao;
import org.kalibro.dao.ReadingGroupDao;

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

		Long readingGroupId = new Random().nextLong();
		doReturn(readingGroupId).when(dto, "readingGroupId");
		whenLazy(ReadingGroupDao.class, "get", readingGroupId).thenReturn(entity.getReadingGroup());
	}

	@Test
	public void shouldAlsoConvertCompoundMetricConfiguration() throws Exception {
		compound = true;
		setUp();
		shouldConvert();
	}

	@Test
	public void readingGroupShouldBeNullForNullReadingGroupId() throws Exception {
		when(dto, "readingGroupId").thenReturn(null);
		assertNull(dto.convert().getReadingGroup());
	}
}