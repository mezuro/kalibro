package org.kalibro.service.entities;

import java.util.Arrays;
import java.util.Collection;

import org.kalibro.DtoTestCase;
import org.kalibro.core.model.CompoundMetric;
import org.kalibro.core.model.CompoundMetricFixtures;

public class CompoundMetricXmlTest extends DtoTestCase<CompoundMetric, CompoundMetricXml> {

	@Override
	protected CompoundMetricXml newDtoUsingDefaultConstructor() {
		return new CompoundMetricXml();
	}

	@Override
	protected Collection<CompoundMetric> entitiesForTestingConversion() {
		return Arrays.asList(CompoundMetricFixtures.sc());
	}

	@Override
	protected CompoundMetricXml createDto(CompoundMetric compoundMetric) {
		return new CompoundMetricXml(compoundMetric);
	}
}