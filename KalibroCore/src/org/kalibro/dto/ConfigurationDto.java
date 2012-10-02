package org.kalibro.dto;

import java.util.SortedSet;

import org.kalibro.Configuration;
import org.kalibro.MetricConfiguration;
import org.kalibro.dao.MetricConfigurationDao;

/**
 * Data transfer object for {@link Configuration}.
 * 
 * @author Carlos Morais
 */
public abstract class ConfigurationDto extends DataTransferObject<Configuration> {

	@Override
	public Configuration convert() {
		Configuration configuration = new Configuration(name());
		setId(configuration, id());
		configuration.setDescription(description() == null ? "" : description());
		configuration.setMetricConfigurations(metricConfigurations());
		return configuration;
	}

	public abstract Long id();

	public abstract String name();

	public abstract String description();

	public SortedSet<MetricConfiguration> metricConfigurations() {
		return DaoLazyLoader.createProxy(MetricConfigurationDao.class, "metricConfigurationsOf", id());
	}
}