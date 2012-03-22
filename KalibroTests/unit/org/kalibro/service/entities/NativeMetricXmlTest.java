package org.kalibro.service.entities;

import static org.junit.Assert.*;
import static org.kalibro.core.model.NativeMetricFixtures.*;

import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.kalibro.DtoTestCase;
import org.kalibro.core.model.NativeMetric;
import org.kalibro.core.model.enums.Language;
import org.powermock.reflect.Whitebox;

public class NativeMetricXmlTest extends DtoTestCase<NativeMetric, NativeMetricXml> {

	@Override
	protected NativeMetricXml newDtoUsingDefaultConstructor() {
		return new NativeMetricXml();
	}

	@Override
	protected Collection<NativeMetric> entitiesForTestingConversion() {
		return nativeMetrics();
	}

	@Override
	protected NativeMetricXml createDto(NativeMetric nativeMetric) {
		return new NativeMetricXml(nativeMetric);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldTurnNullDescriptionAndLanguagesToEmpty() {
		NativeMetric metric = nativeMetric("dit");
		metric.setDescription(null);
		Whitebox.setInternalState(metric, "languages", (List<Language>) null);
		NativeMetric converted = createDto(metric).convert();
		assertEquals("", converted.getDescription());
		assertTrue(converted.getLanguages().isEmpty());
	}
}