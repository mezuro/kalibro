package org.kalibro.service.xml;

import static org.kalibro.core.model.MetricConfigurationFixtures.metricConfiguration;

import org.kalibro.core.model.MetricConfiguration;

public class MetricConfigurationXmlTest extends XmlTest<MetricConfiguration> {

	@Override
	protected MetricConfiguration loadFixture() {
		return metricConfiguration("loc");
	}
}