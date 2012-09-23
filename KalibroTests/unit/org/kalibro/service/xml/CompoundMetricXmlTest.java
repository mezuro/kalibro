package org.kalibro.service.xml;

import static org.kalibro.core.model.MetricFixtures.sc;

import java.util.Arrays;
import java.util.Collection;

import org.kalibro.DtoTestCase;
import org.kalibro.core.model.CompoundMetric;

public class CompoundMetricXmlTest extends DtoTestCase<CompoundMetric, CompoundMetricXml> {

	@Override
	protected CompoundMetricXml newDtoUsingDefaultConstructor() {
		return new CompoundMetricXml();
	}

	@Override
	protected Collection<CompoundMetric> entitiesForTestingConversion() {
		return Arrays.asList(sc());
	}

	@Override
	protected CompoundMetricXml createDto(CompoundMetric compoundMetric) {
		return new CompoundMetricXml(compoundMetric);
	}
}