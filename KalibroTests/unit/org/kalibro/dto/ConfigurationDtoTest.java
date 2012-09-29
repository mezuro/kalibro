package org.kalibro.dto;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.kalibro.Configuration;
import org.kalibro.dao.MetricConfigurationDao;

public class ConfigurationDtoTest extends AbstractDtoTest<Configuration> {

	@Override
	protected Configuration loadFixture() {
		return loadFixture("analizo", Configuration.class);
	}

	@Override
	protected List<LazyLoadExpectation> lazyLoadExpectations() {
		return asList(expectLazy(entity.getMetricConfigurations(),
			MetricConfigurationDao.class, "metricConfigurationsOf", entity.getId()));
	}

	@Test
	public void shouldConvertNullDescriptionIntoEmptyString() throws Exception {
		when(dto, "description").thenReturn(null);
		assertEquals("", dto.convert().getDescription());
	}
}