package org.kalibro.service.xml;

import static org.kalibro.core.model.MetricConfigurationFixtures.metricConfiguration;

import org.kalibro.MetricConfiguration;

public class MetricConfigurationXmlTest extends XmlTest<MetricConfiguration> {

	@Override
	protected MetricConfiguration loadFixture() {
		return metricConfiguration("loc");
	}
}