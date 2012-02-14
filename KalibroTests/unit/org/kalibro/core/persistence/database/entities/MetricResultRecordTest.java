package org.kalibro.core.persistence.database.entities;

import static org.junit.Assert.*;
import static org.kalibro.core.model.ConfigurationFixtures.*;
import static org.kalibro.core.model.MetricResultFixtures.*;
import static org.kalibro.core.model.ModuleFixtures.*;
import static org.kalibro.core.model.ModuleResultFixtures.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.kalibro.DtoTestCase;
import org.kalibro.core.model.MetricResult;
import org.kalibro.core.model.ModuleResult;
import org.kalibro.core.persistence.database.entities.MetricResultRecord;

public class MetricResultRecordTest extends DtoTestCase<MetricResult, MetricResultRecord> {

	@Override
	protected MetricResultRecord newDtoUsingDefaultConstructor() {
		return new MetricResultRecord();
	}

	@Override
	protected Collection<MetricResult> entitiesForTestingConversion() {
		Double[] specialvalues = new Double[]{Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY};
		MetricResult metricResult = metricResult("loc", 42.0, specialvalues);
		return Arrays.asList(metricResult);
	}

	@Override
	protected MetricResultRecord createDto(MetricResult metricResult) {
		return new MetricResultRecord(metricResult, helloWorldClass(), "", new Date());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldConvertModuleResult() {
		Date date = new Date();
		ModuleResult moduleResult = helloWorldClassResult(date);
		List<MetricResultRecord> metricResults = MetricResultRecord.createRecords(moduleResult, "", date);
		List<ModuleResult> converted = MetricResultRecord.convertIntoModuleResults(metricResults);

		assertEquals(1, converted.size());
		ModuleResult convertedResult = converted.get(0);
		convertedResult.setConfiguration(kalibroConfiguration());
		assertDeepEquals(moduleResult, convertedResult);
	}
}