package org.kalibro.core.persistence.database.entities;

import static org.analizo.AnalizoStub.*;

import java.util.Collection;

import org.kalibro.DtoTestCase;
import org.kalibro.core.model.NativeMetric;

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