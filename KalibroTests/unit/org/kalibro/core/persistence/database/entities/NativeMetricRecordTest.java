package org.kalibro.core.persistence.database.entities;

import static org.kalibro.core.model.NativeMetricFixtures.*;

import java.util.Collection;

import org.kalibro.DtoTestCase;
import org.kalibro.core.model.NativeMetric;
import org.kalibro.core.persistence.database.entities.NativeMetricRecord;

public class NativeMetricRecordTest extends DtoTestCase<NativeMetric, NativeMetricRecord> {

	@Override
	protected NativeMetricRecord newDtoUsingDefaultConstructor() {
		return new NativeMetricRecord();
	}

	@Override
	protected Collection<NativeMetric> entitiesForTestingConversion() {
		return nativeMetrics();
	}

	@Override
	protected NativeMetricRecord createDto(NativeMetric nativeMetric) {
		return new NativeMetricRecord(nativeMetric);
	}
}