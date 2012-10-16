package org.kalibro.core.persistence.record;

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
		assertOneToMany("ranges").isMappedBy("configuration");
	}

	@Test
	public void shouldAlsoConvertCompoundMetric() {
		CompoundMetric metric = loadFixture("sc", CompoundMetric.class);
		assertDeepEquals(metric, new MetricConfigurationRecord(new MetricConfiguration(metric)).metric());
	}
}