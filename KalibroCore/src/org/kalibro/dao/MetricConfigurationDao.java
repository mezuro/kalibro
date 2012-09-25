package org.kalibro.dao;

import org.kalibro.MetricConfiguration;

public interface MetricConfigurationDao {

	void save(MetricConfiguration metricConfiguration, String configurationName);

	MetricConfiguration getMetricConfiguration(String configurationName, String metricName);

	void removeMetricConfiguration(String configurationName, String metricName);
}