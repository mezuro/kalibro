package org.kalibro.service.entities;

import static org.junit.Assert.assertTrue;
import static org.kalibro.core.model.ConfigurationFixtures.newConfiguration;
import static org.kalibro.core.model.ModuleResultFixtures.newHelloWorldClassResult;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.kalibro.DtoTestCase;
import org.kalibro.core.model.CompoundMetric;
import org.kalibro.core.model.MetricResult;
import org.kalibro.core.model.ModuleResult;
import org.powermock.reflect.Whitebox;

public class MetricResultXmlTest extends DtoTestCase<MetricResult, MetricResultXml> {

	@Override
	protected MetricResultXml newDtoUsingDefaultConstructor() {
		return new MetricResultXml();
	}

	@Override
	protected Collection<MetricResult> entitiesForTestingConversion() {
		ModuleResult moduleResult = newHelloWorldClassResult();
		moduleResult.setConfiguration(newConfiguration("loc"));
		MetricResult compoundResult = new MetricResult(new CompoundMetric(), 42.0);
		Set<MetricResult> results = new HashSet<MetricResult>(moduleResult.getMetricResults());
		results.add(compoundResult);
		return results;
	}

	@Override
	protected MetricResultXml createDto(MetricResult metricResult) {
		return new MetricResultXml(metricResult);
	}

	@Test
	public void shouldTurnNullDescendentResultsToEmpty() {
		MetricResult metricResult = new MetricResult(new CompoundMetric(), 42.0);
		MetricResultXml dto = createDto(metricResult);
		Whitebox.setInternalState(dto, "descendentResults", (Collection<Double>) null);
		assertTrue(dto.convert().getDescendentResults().isEmpty());
	}
}