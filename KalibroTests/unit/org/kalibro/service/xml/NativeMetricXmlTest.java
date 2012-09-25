package org.kalibro.service.xml;

import org.kalibro.NativeMetric;
import org.kalibro.core.model.MetricFixtures;

public class NativeMetricXmlTest extends XmlTest<NativeMetric> {

	@Override
	protected NativeMetric loadFixture() {
		return MetricFixtures.analizoMetric("amloc");
	}
}