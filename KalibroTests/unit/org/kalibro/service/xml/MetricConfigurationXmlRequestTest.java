package org.kalibro.service.xml;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.kalibro.Statistic;

public class MetricConfigurationXmlRequestTest extends XmlTest {

	@Override
	public void verifyElements() {
		assertElement("id", Long.class);
		assertElement("code", String.class, true);
		assertElement("metric", MetricXmlResponse.class, true);
		assertElement("baseTool", BaseToolXml.class);
		assertElement("weight", Double.class, true);
		assertElement("aggregationForm", Statistic.class, true);
		assertElement("readingGroupId", Long.class);
		assertCollection("range");
	}

	@Test
	public void shouldConvertNullRangesIntoEmptyCollection() {
		assertTrue(new MetricConfigurationXmlRequest().ranges().isEmpty());
	}
}