package org.kalibro.core.persistence.entities;

import static org.junit.Assert.assertEquals;
import static org.kalibro.core.model.ConfigurationFixtures.*;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.kalibro.DtoTestCase;
import org.kalibro.core.model.Configuration;

public class ConfigurationRecordTest extends DtoTestCase<Configuration, ConfigurationRecord> {

	@Override
	protected ConfigurationRecord newDtoUsingDefaultConstructor() {
		return new ConfigurationRecord();
	}

	@Override
	protected Collection<Configuration> entitiesForTestingConversion() {
		return Arrays.asList(kalibroConfiguration());
	}

	@Override
	protected ConfigurationRecord createDto(Configuration configuration) {
		return new ConfigurationRecord(configuration);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRetrieveName() {
		Configuration configuration = newConfiguration();
		assertEquals(configuration.getName(), createDto(configuration).getName());
	}
}