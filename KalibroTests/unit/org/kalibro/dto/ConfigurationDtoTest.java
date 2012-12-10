package org.kalibro.dto;

import static org.junit.Assert.assertEquals;

import java.util.Random;

import org.junit.Test;
import org.kalibro.Configuration;
import org.kalibro.dao.MetricConfigurationDao;
import org.powermock.reflect.Whitebox;

public class ConfigurationDtoTest extends AbstractDtoTest<Configuration> {

	@Override
	protected Configuration loadFixture() {
		Configuration configuration = loadFixture("sc", Configuration.class);
		Whitebox.setInternalState(configuration, "id", new Random().nextLong());
		return configuration;
	}

	@Override
	protected void registerLazyLoadExpectations() {
		whenLazy(MetricConfigurationDao.class, "metricConfigurationsOf", entity.getId())
			.thenReturn(entity.getMetricConfigurations());
	}

	@Test
	public void shouldConvertNullDescriptionIntoEmptyString() throws Exception {
		when(dto, "description").thenReturn(null);
		assertEquals("", dto.convert().getDescription());
	}
}