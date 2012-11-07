package org.kalibro.service.xml;

import static org.junit.Assert.*;

import org.junit.Test;
import org.kalibro.MetricConfiguration;
import org.kalibro.Statistic;

public class MetricConfigurationXmlTest extends XmlTest {

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
		MetricConfigurationXml record = (MetricConfigurationXml) dto;
		assertEquals(range.getReadingGroup().getId(), record.readingGroupId());
	}

	@Test
	public void checkNullReadingGroup() {
		assertNull(new MetricConfigurationXml().readingGroupId());
	}
}