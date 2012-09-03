package org.kalibro.core.persistence.entities;

import static org.junit.Assert.assertEquals;
import static org.kalibro.core.model.MetricResultFixtures.newMetricResult;
import static org.kalibro.core.model.ModuleFixtures.helloWorldClass;
import static org.kalibro.core.model.ModuleResultFixtures.newHelloWorldClassResult;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.DtoTestCase;
import org.kalibro.core.model.MetricResult;
import org.kalibro.core.model.ModuleResult;
import org.kalibro.core.model.ProjectResult;
import org.kalibro.core.model.ProjectResultFixtures;

public class MetricResultRecordTest extends DtoTestCase<MetricResult, MetricResultRecord> {

	private ProjectResult projectResult;

	@Before
	public void setUp() {
		projectResult = ProjectResultFixtures.helloWorldResult();
	}

	@Override
	protected MetricResultRecord newDtoUsingDefaultConstructor() {
		return new MetricResultRecord();
	}

	@Override
	protected Collection<MetricResult> entitiesForTestingConversion() {
		Double[] specialvalues = new Double[]{Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY};
		MetricResult metricResult = newMetricResult("loc", 42.0, specialvalues);
		return Arrays.asList(metricResult);
	}

	@Override
	protected MetricResultRecord createDto(MetricResult metricResult) {
		return new MetricResultRecord(metricResult, helloWorldClass(), projectResult);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldConvertModuleResult() {
		ModuleResult moduleResult = newHelloWorldClassResult(projectResult.getDate());
		List<MetricResultRecord> metricResults = MetricResultRecord.createRecords(moduleResult, projectResult);
		List<ModuleResult> converted = MetricResultRecord.convertIntoModuleResults(metricResults);

		assertEquals(1, converted.size());
		ModuleResult convertedResult = converted.get(0);
		assertDeepEquals(moduleResult, convertedResult);
	}
}