package org.kalibro.core.persistence.record;

import static org.kalibro.core.model.BaseToolFixtures.analizoStub;

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
		return analizoStub().getSupportedMetrics();
	}

	@Override
	protected NativeMetricRecord createDto(NativeMetric nativeMetric) {
		return new NativeMetricRecord(nativeMetric);
	}
}