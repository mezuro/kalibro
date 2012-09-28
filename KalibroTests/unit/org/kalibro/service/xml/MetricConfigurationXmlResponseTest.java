package org.kalibro.service.xml;

import static org.kalibro.MetricConfigurationFixtures.metricConfiguration;

import org.kalibro.MetricConfiguration;

public class MetricConfigurationXmlResponseTest extends XmlTest<MetricConfiguration> {

	@Override
	protected MetricConfiguration loadFixture() {
		return metricConfiguration("loc");
	}
}