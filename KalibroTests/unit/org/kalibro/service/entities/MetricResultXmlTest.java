package org.kalibro.service.entities;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.kalibro.DtoTestCase;
import org.kalibro.core.model.*;

public class MetricResultXmlTest extends DtoTestCase<MetricResult, MetricResultXml> {

	@Override
	protected MetricResultXml newDtoUsingDefaultConstructor() {
		return new MetricResultXml();
	}

	@Override
	protected Collection<MetricResult> entitiesForTestingConversion() {
		ModuleResult moduleResult = ModuleResultFixtures.helloWorldClassResult();
		moduleResult.setConfiguration(ConfigurationFixtures.simpleConfiguration());
		MetricResult compoundResult = new MetricResult(new CompoundMetric(), 42.0);
		Set<MetricResult> results = new HashSet<MetricResult>(moduleResult.getMetricResults());
		results.add(compoundResult);
		return results;
	}

	@Override
	protected MetricResultXml createDto(MetricResult metricResult) {
		return new MetricResultXml(metricResult);
	}
}