package org.kalibro.service.entities;

import static org.kalibro.core.model.NativeMetricFixtures.*;

import java.util.Collection;

import org.kalibro.DtoTestCase;
import org.kalibro.core.model.NativeMetric;

public class NativeMetricXmlTest extends DtoTestCase<NativeMetric, NativeMetricXml> {

	@Override
	protected NativeMetricXml newDtoUsingDefaultConstructor() {
		return new NativeMetricXml();
	}

	@Override
	protected Collection<NativeMetric> entitiesForTestingConversion() {
		return nativeMetrics();
	}

	@Override
	protected NativeMetricXml createDto(NativeMetric nativeMetric) {
		return new NativeMetricXml(nativeMetric);
	}
}