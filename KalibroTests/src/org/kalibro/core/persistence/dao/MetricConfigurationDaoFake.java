package org.kalibro.core.persistence.dao;

import java.util.HashMap;
import java.util.Map;

import org.kalibro.core.abstractentity.AbstractEntity;
import org.kalibro.core.abstractentity.IdentityField;
import org.kalibro.core.model.MetricConfiguration;

public class MetricConfigurationDaoFake implements MetricConfigurationDao {

	private Map<MetricConfigurationKey, MetricConfiguration> metricConfigurations =
		new HashMap<MetricConfigurationKey, MetricConfiguration>();

	@Override
	public void save(MetricConfiguration metricConfiguration, String configurationName) {
		MetricConfigurationKey key = new MetricConfigurationKey(metricConfiguration, configurationName);
		metricConfigurations.put(key, metricConfiguration);
	}

	@Override
	public MetricConfiguration getMetricConfiguration(String configurationName, String metricName) {
		MetricConfigurationKey key = new MetricConfigurationKey(metricName, configurationName);
		return metricConfigurations.get(key);
	}

	@Override
	public void removeMetricConfiguration(String configurationName, String metricName) {
		MetricConfigurationKey key = new MetricConfigurationKey(metricName, configurationName);
		metricConfigurations.remove(key);
	}

	private class MetricConfigurationKey extends AbstractEntity<MetricConfigurationKey> {

		@IdentityField
		@SuppressWarnings("unused" /* used via reflection */)
		private String metricName;

		@IdentityField
		@SuppressWarnings("unused" /* used via reflection */)
		private String configurationName;

		private MetricConfigurationKey(MetricConfiguration metricConfiguration, String configurationName) {
			this(metricConfiguration.getMetric().getName(), configurationName);
		}

		public MetricConfigurationKey(String metricName, String configurationName) {
			this.metricName = metricName;
			this.configurationName = configurationName;
		}
	}
}