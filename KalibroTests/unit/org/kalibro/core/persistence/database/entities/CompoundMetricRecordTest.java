package org.kalibro.core.persistence.database.entities;

import java.util.Arrays;
import java.util.Collection;

import org.kalibro.DtoTestCase;
import org.kalibro.core.model.CompoundMetric;
import org.kalibro.core.model.CompoundMetricFixtures;

public class CompoundMetricRecordTest extends DtoTestCase<CompoundMetric, CompoundMetricRecord> {

	@Override
	protected CompoundMetricRecord newDtoUsingDefaultConstructor() {
		return new CompoundMetricRecord();
	}

	@Override
	protected Collection<CompoundMetric> entitiesForTestingConversion() {
		return Arrays.asList(CompoundMetricFixtures.sc());
	}

	@Override
	protected CompoundMetricRecord createDto(CompoundMetric compoundMetric) {
		return new CompoundMetricRecord(compoundMetric, null);
	}
}