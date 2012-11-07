package org.kalibro.dto;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.kalibro.Configuration;
import org.kalibro.dao.MetricConfigurationDao;

public class ConfigurationDtoTest extends AbstractDtoTest<Configuration> {

	@Override
	protected Configuration loadFixture() {
		return loadFixture("sc", Configuration.class);
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