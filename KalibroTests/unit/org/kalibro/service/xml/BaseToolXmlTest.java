package org.kalibro.service.xml;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class BaseToolXmlTest extends XmlTest {

	@Override
	public void verifyElements() {
		assertElement("name", String.class);
		assertElement("description", String.class);
		assertElement("collectorClassName", String.class);
		assertCollection("supportedMetric");
	}

	@Test
	public void shouldConvertNullSupportedMetricsIntoEmptyCollection() {
		assertTrue(new BaseToolXml().supportedMetrics().isEmpty());
	}
}