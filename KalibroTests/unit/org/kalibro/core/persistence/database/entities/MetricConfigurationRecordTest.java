package org.kalibro.core.persistence.database.entities;

import static org.kalibro.core.model.ConfigurationFixtures.*;
import static org.kalibro.core.model.MetricFixtures.*;

import java.util.ArrayList;
import java.util.Collection;

import org.kalibro.DtoTestCase;
import org.kalibro.core.model.MetricConfiguration;

public class MetricConfigurationRecordTest extends DtoTestCase<MetricConfiguration, MetricConfigurationRecord> {

	@Override
	protected MetricConfigurationRecord newDtoUsingDefaultConstructor() {
		return new MetricConfigurationRecord();
	}

	@Override
	protected Collection<MetricConfiguration> entitiesForTestingConversion() {
		Collection<MetricConfiguration> configurations = kalibroConfiguration().getMetricConfigurations();
		ArrayList<MetricConfiguration> entities = new ArrayList<MetricConfiguration>(configurations);
		entities.add(new MetricConfiguration(sc()));
		return entities;
	}

	@Override
	protected MetricConfigurationRecord createDto(MetricConfiguration metricConfiguration) {
		return new MetricConfigurationRecord(metricConfiguration, null);
	}
}