package org.kalibro.core.persistence.record;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.BaseTool;
import org.kalibro.CompoundMetric;
import org.kalibro.MetricConfiguration;
import org.kalibro.dao.BaseToolDao;
import org.kalibro.dao.DaoFactory;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DaoFactory.class)
public class MetricConfigurationRecordTest extends RecordTest {

	@Override
	public void setUp() throws Exception {
		super.setUp();
		mockBaseTool();
	}

	private void mockBaseTool() {
		BaseToolDao baseToolDao = mock(BaseToolDao.class);
		mockStatic(DaoFactory.class);
		when(DaoFactory.getBaseToolDao()).thenReturn(baseToolDao);
		when(baseToolDao.get("Inexistent")).thenReturn(loadFixture("inexistent", BaseTool.class));
	}

	@Override
	protected void verifyColumns() {
		assertManyToOne("configuration", ConfigurationRecord.class).isRequired();
		shouldHaveId();
		assertColumn("code", String.class).isRequired();
		assertColumn("weight", Long.class).isRequired();
		assertColumn("aggregationForm", String.class).isRequired();
		assertManyToOne("readingGroup", ReadingGroupRecord.class).isOptional();
		assertColumn("compound", Boolean.class).isRequired();
		assertColumn("metricName", String.class).isRequired();
		assertColumn("metricScope", String.class).isRequired();
		assertColumn("metricDescription", String.class).isNullable();
		assertColumn("metricOrigin", String.class).isRequired();
		assertOneToMany("ranges").doesNotCascade().isMappedBy("configuration");
	}

	@Test
	public void shouldAlsoConvertCompoundMetric() {
		CompoundMetric metric = loadFixture("sc", CompoundMetric.class);
		assertDeepEquals(metric, new MetricConfigurationRecord(new MetricConfiguration(metric)).metric());
	}

	@Test
	public void shouldRetrieveReadingGroupId() {
		MetricConfiguration range = (MetricConfiguration) entity;
		MetricConfigurationRecord record = (MetricConfigurationRecord) dto;
		assertEquals(range.getReadingGroup().getId(), record.readingGroupId());
	}

	@Test
	public void checkNullReadingGroup() {
		assertNull(new MetricConfigurationRecord(new MetricConfiguration()).readingGroupId());
	}
}