package org.kalibro.service.xml;

public class BaseToolXmlTest extends XmlTest {

	@Override
	public void verifyElements() {
		assertElement("name", String.class, false);
		assertElement("description", String.class, false);
		assertElement("collectorClassName", String.class, false);
		assertCollection("supportedMetrics", false, "supportedMetric");
	}
}