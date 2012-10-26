package org.kalibro.service.xml;

import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.MetricConfiguration;
import org.kalibro.ReadingGroup;
import org.kalibro.Statistic;
import org.kalibro.dao.ReadingGroupDao;
import org.kalibro.dto.DaoLazyLoader;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DaoLazyLoader.class)
public class MetricConfigurationXmlTest extends XmlTest {

	private static final long READING_GROUP_ID = 42L;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		Whitebox.setInternalState(dto, "readingGroupId", READING_GROUP_ID);
		ReadingGroup readingGroup = Whitebox.getInternalState(entity, "readingGroup");
		mockStatic(DaoLazyLoader.class);
		when(DaoLazyLoader.createProxy(ReadingGroupDao.class, "get", READING_GROUP_ID)).thenReturn(readingGroup);
	}

	@Override
	protected void verifyElements() {
		assertElement("id", Long.class);
		assertElement("code", String.class, true);
		assertElement("metric", MetricXml.class, true);
		assertElement("baseToolName", String.class);
		assertElement("weight", Double.class, true);
		assertElement("aggregationForm", Statistic.class, true);
		assertElement("readingGroupId", Long.class);
	}

	@Test
	public void shouldReturnNullBaseToolForCompoundMetricConfiguration() {
		assertNull(new MetricConfigurationXml(new MetricConfiguration()).baseToolName());
	}

	@Test
	public void checkNullReadingGroup() {
		assertNull(new MetricConfigurationXml().readingGroup());
	}
}