package org.kalibro.service.xml;

import org.kalibro.core.model.MetricFixtures;
import org.kalibro.core.model.NativeMetric;

public class NativeMetricXmlTest extends XmlTest<NativeMetric> {

	@Override
	protected NativeMetric loadFixture() {
		return MetricFixtures.analizoMetric("amloc");
	}
}