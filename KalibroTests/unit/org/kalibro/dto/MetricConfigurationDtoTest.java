package org.kalibro.dto;

import java.util.SortedSet;

import org.junit.Test;
import org.kalibro.CompoundMetric;
import org.kalibro.MetricConfiguration;
import org.kalibro.Range;
import org.kalibro.ReadingGroup;
import org.kalibro.dao.RangeDao;
import org.kalibro.dao.ReadingGroupDao;

public class MetricConfigurationDtoTest extends AbstractDtoTest<MetricConfiguration> {

	@Override
	protected MetricConfiguration loadFixture() {
		return loadFixture("lcom4", MetricConfiguration.class);
	}

	@Override
	protected void registerLazyLoadExpectations() {
		whenLazy(ReadingGroupDao.class, "readingGroupOf", entity.getId()).thenReturn(entity.getReadingGroup());
		whenLazy(RangeDao.class, "rangesOf", entity.getId()).thenReturn(entity.getRanges());
	}

	@Test
	public void shouldAlsoConvertCompoundMetricConfiguration() throws Exception {
		ReadingGroup readingGroup = entity.getReadingGroup();
		SortedSet<Range> ranges = entity.getRanges();
		entity = new MetricConfiguration(loadFixture("sc", CompoundMetric.class));
		entity.setReadingGroup(readingGroup);
		entity.setRanges(ranges);
		createDto();
		shouldConvert();
	}
}