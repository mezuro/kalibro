package org.kalibro.service.entities;

import static org.kalibro.core.model.ModuleResultFixtures.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.kalibro.DtoTestCase;
import org.kalibro.core.model.CompoundMetric;
import org.kalibro.core.model.MetricResult;

public class MetricResultXmlTest extends DtoTestCase<MetricResult, MetricResultXml> {

	@Override
	protected MetricResultXml newDtoUsingDefaultConstructor() {
		return new MetricResultXml();
	}

	@Override
	protected Collection<MetricResult> entitiesForTestingConversion() {
		MetricResult compoundResult = new MetricResult(new CompoundMetric(), 42.0);
		Set<MetricResult> results = new HashSet<MetricResult>(helloWorldClassResult().getMetricResults());
		results.add(compoundResult);
		return results;
	}

	@Override
	protected MetricResultXml createDto(MetricResult metricResult) {
		return new MetricResultXml(metricResult);
	}
}