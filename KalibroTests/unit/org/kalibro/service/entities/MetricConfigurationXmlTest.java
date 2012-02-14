package org.kalibro.service.entities;

import static org.kalibro.core.model.ConfigurationFixtures.*;

import java.util.ArrayList;
import java.util.Collection;

import org.kalibro.DtoTestCase;
import org.kalibro.core.model.CompoundMetricFixtures;
import org.kalibro.core.model.MetricConfiguration;

public class MetricConfigurationXmlTest extends DtoTestCase<MetricConfiguration, MetricConfigurationXml> {

	@Override
	protected MetricConfigurationXml newDtoUsingDefaultConstructor() {
		return new MetricConfigurationXml();
	}

	@Override
	protected Collection<MetricConfiguration> entitiesForTestingConversion() {
		Collection<MetricConfiguration> configurations = kalibroConfiguration().getMetricConfigurations();
		ArrayList<MetricConfiguration> entities = new ArrayList<MetricConfiguration>(configurations);
		entities.add(new MetricConfiguration(CompoundMetricFixtures.sc()));
		return entities;
	}

	@Override
	protected MetricConfigurationXml createDto(MetricConfiguration metricConfiguration) {
		return new MetricConfigurationXml(metricConfiguration);
	}
}