package org.kalibro.service.xml;

import static org.kalibro.MetricFixtures.sc;

import org.kalibro.CompoundMetric;

public class CompoundMetricXmlTest extends XmlTest<CompoundMetric> {

	@Override
	protected CompoundMetric loadFixture() {
		return sc();
	}
}