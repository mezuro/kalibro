package org.kalibro.service.xml;

import static org.kalibro.core.model.MetricFixtures.sc;

import org.kalibro.core.model.CompoundMetric;

public class CompoundMetricXmlTest extends XmlTest<CompoundMetric> {

	@Override
	protected CompoundMetric loadFixture() {
		return sc();
	}
}