package org.kalibro.service.xml;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.MetricConfiguration;
import org.kalibro.Statistic;
import org.kalibro.dao.RangeDao;
import org.kalibro.dto.DaoLazyLoader;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DaoLazyLoader.class)
public class MetricConfigurationXmlTest extends XmlTest {

	@Override
	public void setUp() throws Exception {
		super.setUp();
		MetricConfiguration configuration = (MetricConfiguration) entity;
		mockStatic(DaoLazyLoader.class);
		when(DaoLazyLoader.createProxy(RangeDao.class, "rangesOf", configuration.getId()))
			.thenReturn(configuration.getRanges());
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
	public void shouldRetrieveReadingGroupId() {
		MetricConfiguration range = (MetricConfiguration) entity;
		MetricConfigurationXml xml = (MetricConfigurationXml) dto;
		assertEquals(range.getReadingGroup().getId(), xml.readingGroupId());
	}

	@Test
	public void checkNullReadingGroup() {
		assertNull(new MetricConfigurationXml().readingGroupId());
	}

	@Test
	public void rangesShouldBeEmptyIfHasNoId() {
		assertTrue(new MetricConfigurationXml().ranges().isEmpty());
	}
}