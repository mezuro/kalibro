package org.kalibro.service.entities;

import static org.junit.Assert.*;
import static org.kalibro.core.model.MetricConfigurationFixtures.*;
import static org.kalibro.core.model.MetricFixtures.*;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.kalibro.DtoTestCase;
import org.kalibro.core.model.MetricConfiguration;
import org.kalibro.core.model.enums.Statistic;
import org.powermock.reflect.Whitebox;

public class MetricConfigurationXmlTest extends DtoTestCase<MetricConfiguration, MetricConfigurationXml> {

	@Override
	protected MetricConfigurationXml newDtoUsingDefaultConstructor() {
		return new MetricConfigurationXml();
	}

	@Override
	protected Collection<MetricConfiguration> entitiesForTestingConversion() {
		return Arrays.asList(metricConfiguration("loc"), new MetricConfiguration(sc()));
	}

	@Override
	protected MetricConfigurationXml createDto(MetricConfiguration metricConfiguration) {
		return new MetricConfigurationXml(metricConfiguration);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldTurnNullUnrequiredFieldsToDefault() {
		MetricConfiguration configuration = metricConfiguration("loc");
		MetricConfigurationXml dto = createDto(configuration);
		Whitebox.setInternalState(dto, "weight", (Object) null);
		Whitebox.setInternalState(dto, "aggregationForm", (Object) null);
		Whitebox.setInternalState(dto, "ranges", (Object) null);
		MetricConfiguration converted = dto.convert();
		assertDoubleEquals(1.0, converted.getWeight());
		assertEquals(Statistic.AVERAGE, converted.getAggregationForm());
		assertTrue(converted.getRanges().isEmpty());
	}
}