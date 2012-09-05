package org.kalibro.core.persistence.record;

import static org.kalibro.core.model.MetricFixtures.sc;

import java.util.Arrays;
import java.util.Collection;

import org.kalibro.DtoTestCase;
import org.kalibro.core.model.CompoundMetric;

public class CompoundMetricRecordTest extends DtoTestCase<CompoundMetric, CompoundMetricRecord> {

	@Override
	protected CompoundMetricRecord newDtoUsingDefaultConstructor() {
		return new CompoundMetricRecord();
	}

	@Override
	protected Collection<CompoundMetric> entitiesForTestingConversion() {
		return Arrays.asList(sc());
	}

	@Override
	protected CompoundMetricRecord createDto(CompoundMetric compoundMetric) {
		return new CompoundMetricRecord(compoundMetric, null);
	}
}