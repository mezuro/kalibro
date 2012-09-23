package org.kalibro.service.xml;

import org.kalibro.core.model.CompoundMetric;
import org.kalibro.core.model.MetricResult;

public class MetricResultXmlTest extends XmlTest<MetricResult> {

	@Override
	protected MetricResult loadFixture() {
		return new MetricResult(new CompoundMetric(), 42.0);
	}
}