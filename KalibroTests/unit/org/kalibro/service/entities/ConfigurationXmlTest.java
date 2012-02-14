package org.kalibro.service.entities;

import static org.junit.Assert.*;
import static org.kalibro.core.model.ConfigurationFixtures.*;

import java.util.Arrays;
import java.util.Collection;

import org.kalibro.DtoTestCase;
import org.kalibro.core.model.Configuration;

public class ConfigurationXmlTest extends DtoTestCase<Configuration, ConfigurationXml> {

	@Override
	public void defaultConstructorShouldDoNothing() {
		Configuration newConfiguration = newDtoUsingDefaultConstructor().convert();
		assertNull(newConfiguration.getName());
		assertNull(newConfiguration.getDescription());
		assertTrue(newConfiguration.getMetricConfigurations().isEmpty());
	}

	@Override
	protected ConfigurationXml newDtoUsingDefaultConstructor() {
		return new ConfigurationXml();
	}

	@Override
	protected Collection<Configuration> entitiesForTestingConversion() {
		return Arrays.asList(kalibroConfiguration());
	}

	@Override
	protected ConfigurationXml createDto(Configuration configuration) {
		return new ConfigurationXml(configuration);
	}
}