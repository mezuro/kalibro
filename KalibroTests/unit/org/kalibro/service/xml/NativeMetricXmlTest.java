package org.kalibro.service.xml;

import org.kalibro.MetricFixtures;
import org.kalibro.NativeMetric;

public class NativeMetricXmlTest extends XmlTest<NativeMetric> {

	@Override
	protected NativeMetric loadFixture() {
		return MetricFixtures.analizoMetric("amloc");
	}
}