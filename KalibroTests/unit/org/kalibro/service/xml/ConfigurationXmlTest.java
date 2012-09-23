package org.kalibro.service.xml;

import static org.kalibro.core.model.ConfigurationFixtures.newConfiguration;

import java.util.Arrays;
import java.util.Collection;

import org.kalibro.Configuration;
import org.kalibro.DtoTestCase;

public class ConfigurationXmlTest extends DtoTestCase<Configuration, ConfigurationXml> {

	@Override
	protected ConfigurationXml newDtoUsingDefaultConstructor() {
		return new ConfigurationXml();
	}

	@Override
	protected Collection<Configuration> entitiesForTestingConversion() {
		return Arrays.asList(newConfiguration("loc"));
	}

	@Override
	protected ConfigurationXml createDto(Configuration configuration) {
		return new ConfigurationXml(configuration);
	}
}