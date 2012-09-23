package org.kalibro.service.xml;

import static org.junit.Assert.*;
import static org.kalibro.core.model.BaseToolFixtures.analizoStub;
import static org.kalibro.core.model.MetricFixtures.analizoMetric;

import java.util.Collection;

import org.junit.Test;
import org.kalibro.DtoTestCase;
import org.kalibro.core.model.NativeMetric;
import org.powermock.reflect.Whitebox;

public class NativeMetricXmlTest extends DtoTestCase<NativeMetric, NativeMetricXml> {

	@Override
	protected NativeMetricXml newDtoUsingDefaultConstructor() {
		return new NativeMetricXml();
	}

	@Override
	protected Collection<NativeMetric> entitiesForTestingConversion() {
		return analizoStub().getSupportedMetrics();
	}

	@Override
	protected NativeMetricXml createDto(NativeMetric nativeMetric) {
		return new NativeMetricXml(nativeMetric);
	}

	@Test
	public void shouldTurnNullDescriptionAndLanguagesToEmpty() {
		NativeMetric metric = analizoMetric("dit");
		NativeMetricXml dto = createDto(metric);
		Whitebox.setInternalState(dto, "description", (Object) null);
		Whitebox.setInternalState(dto, "languages", (Object) null);
		NativeMetric converted = dto.convert();
		assertEquals("", converted.getDescription());
		assertTrue(converted.getLanguages().isEmpty());
	}
}