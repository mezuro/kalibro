package org.kalibro.dao;

import java.util.SortedSet;

import org.kalibro.MetricConfiguration;

/**
 * Data access object for {@link MetricConfiguration}.
 * 
 * @author Carlos Morais
 */
public interface MetricConfigurationDao {

	MetricConfiguration get(Long metricConfigurationId);

	SortedSet<MetricConfiguration> metricConfigurationsOf(Long configurationId);

	Long save(MetricConfiguration metricConfiguration, Long configurationId);

	void delete(Long metricConfigurationId);
}