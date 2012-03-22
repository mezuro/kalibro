package org.kalibro.service.entities;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.kalibro.DtoTestCase;
import org.kalibro.core.model.*;
import org.powermock.reflect.Whitebox;

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

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldTurnNullDescendentResultsToEmpty() {
		MetricResult metricResult = new MetricResult(new CompoundMetric(), 42.0);
		MetricResultXml dto = createDto(metricResult);
		Whitebox.setInternalState(dto, "descendentResults", (Collection<Double>) null);
		assertTrue(dto.convert().getDescendentResults().isEmpty());
	}
}