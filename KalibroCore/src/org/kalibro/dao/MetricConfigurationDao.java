package org.kalibro.dao;

import java.util.SortedSet;

import org.kalibro.MetricConfiguration;

public interface MetricConfigurationDao {

	void save(MetricConfiguration metricConfiguration, String configurationName);

	MetricConfiguration getMetricConfiguration(String configurationName, String metricName);

	void removeMetricConfiguration(String configurationName, String metricName);

	SortedSet<MetricConfiguration> metricConfigurationsOf(Long configurationId);
}