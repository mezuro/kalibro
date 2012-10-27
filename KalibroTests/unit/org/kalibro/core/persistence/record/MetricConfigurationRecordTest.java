package org.kalibro.core.persistence.record;

import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.BaseTool;
import org.kalibro.CompoundMetric;
import org.kalibro.MetricConfiguration;
import org.kalibro.ReadingGroup;
import org.kalibro.dao.BaseToolDao;
import org.kalibro.dao.DaoFactory;
import org.kalibro.dao.ReadingGroupDao;
import org.kalibro.dto.DaoLazyLoader;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
@PrepareForTest({DaoFactory.class, DaoLazyLoader.class})
public class MetricConfigurationRecordTest extends RecordTest {

	@Override
	public void setUp() throws Exception {
		super.setUp();
		mockBaseTool();
		mockReadingGroup();
	}

	private void mockBaseTool() {
		BaseToolDao baseToolDao = mock(BaseToolDao.class);
		mockStatic(DaoFactory.class);
		when(DaoFactory.getBaseToolDao()).thenReturn(baseToolDao);
		when(baseToolDao.get("Inexistent")).thenReturn(loadFixture("inexistent", BaseTool.class));
	}

	private void mockReadingGroup() {
		Long id = Whitebox.getInternalState(entity, "id");
		ReadingGroup readingGroup = Whitebox.getInternalState(entity, "readingGroup");
		mockStatic(DaoLazyLoader.class);
		when(DaoLazyLoader.createProxy(ReadingGroupDao.class, "readingGroupOf", id)).thenReturn(readingGroup);
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
	public void checkNullReadingGroup() {
		assertNull(new MetricConfigurationRecord(new MetricConfiguration()).readingGroup());
	}
}