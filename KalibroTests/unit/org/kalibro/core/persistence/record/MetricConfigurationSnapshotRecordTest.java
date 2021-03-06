package org.kalibro.core.persistence.record;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.BaseTool;
import org.kalibro.CompoundMetric;
import org.kalibro.MetricConfiguration;
import org.kalibro.ReadingGroup;
import org.kalibro.dao.BaseToolDao;
import org.kalibro.dao.DaoFactory;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DaoFactory.class)
public class MetricConfigurationSnapshotRecordTest extends RecordTest {

	@Override
	public void setUp() throws Exception {
		super.setUp();
		BaseToolDao baseToolDao = mock(BaseToolDao.class);
		mockStatic(DaoFactory.class);
		when(DaoFactory.getBaseToolDao()).thenReturn(baseToolDao);
		when(baseToolDao.get("Inexistent")).thenReturn(loadFixture("inexistent", BaseTool.class));
		Whitebox.setInternalState(entity, "id", (Long) null);
	}

	@Override
	protected String entityName() {
		return "MetricConfigurationSnapshot";
	}

	@Override
	protected void verifyColumns() {
		shouldHaveId();
		assertColumn("processing", Long.class).isRequired();
		assertColumn("code", String.class).isRequired();
		assertColumn("weight", Long.class).isRequired();
		assertColumn("aggregationForm", String.class).isRequired();
		assertColumn("compound", Boolean.class).isRequired();
		assertColumn("metricName", String.class).isRequired();
		assertColumn("metricScope", String.class).isRequired();
		assertColumn("metricDescription", String.class).isNullable();
		assertColumn("metricOrigin", String.class).isRequired();
		assertOneToMany("ranges").isLazy().isMappedBy("configurationSnapshot");
	}

	@Test
	public void shouldAlsoConvertCompoundMetric() {
		CompoundMetric metric = loadFixture("sc", CompoundMetric.class);
		assertDeepEquals(metric, new MetricConfigurationSnapshotRecord(new MetricConfiguration(metric)).metric());
	}

	@Test
	public void shouldNotPreserveReadingGroup() {
		MetricConfiguration metricConfiguration = new MetricConfiguration();
		metricConfiguration.setReadingGroup(loadFixture("scholar", ReadingGroup.class));
		MetricConfigurationSnapshotRecord snapshot = new MetricConfigurationSnapshotRecord(metricConfiguration);
		assertNull(snapshot.readingGroupId());
	}

	@Test
	public void shouldHaveIdConstructor() {
		Long id = mock(Long.class);
		assertSame(id, new MetricConfigurationSnapshotRecord(id).id());
	}
}