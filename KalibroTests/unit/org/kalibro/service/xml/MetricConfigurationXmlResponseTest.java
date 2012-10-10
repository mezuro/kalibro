package org.kalibro.service.xml;

import org.kalibro.Statistic;

public class MetricConfigurationXmlResponseTest extends XmlTest {

	@Override
	public void verifyElements() {
		assertElement("id", Long.class);
		assertElement("code", String.class);
		assertElement("metric", MetricXmlResponse.class);
		assertElement("baseTool", BaseToolXml.class);
		assertElement("weight", Double.class);
		assertElement("aggregationForm", Statistic.class);
	}
}