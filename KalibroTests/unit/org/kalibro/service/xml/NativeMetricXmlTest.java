package org.kalibro.service.xml;

import org.kalibro.NativeMetric;

public class NativeMetricXmlTest extends XmlTest<NativeMetric> {

	@Override
	protected NativeMetric loadFixture() {
		return loadFixture("lcom4", NativeMetric.class);
	}
}