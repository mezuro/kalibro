package org.kalibro.core.dao;

import org.kalibro.core.model.MetricConfiguration;

public interface MetricConfigurationDao {

	void save(MetricConfiguration metricConfiguration, String configurationName);

	MetricConfiguration getMetricConfiguration(String configurationName, String metricName);

	void removeMetricConfiguration(String configurationName, String metricName);
}