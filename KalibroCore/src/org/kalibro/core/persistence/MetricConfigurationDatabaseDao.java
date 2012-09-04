package org.kalibro.core.persistence;

import org.kalibro.core.model.Configuration;
import org.kalibro.core.model.MetricConfiguration;
import org.kalibro.core.persistence.dao.MetricConfigurationDao;
import org.kalibro.core.persistence.entities.MetricConfigurationRecord;
import org.kalibro.core.persistence.entities.RangeRecord;

class MetricConfigurationDatabaseDao extends DatabaseDao<MetricConfiguration, MetricConfigurationRecord> implements
	MetricConfigurationDao {

	private ConfigurationDatabaseDao configurationDao;

	protected MetricConfigurationDatabaseDao(DatabaseManager databaseManager) {
		super(databaseManager, MetricConfigurationRecord.class);
		configurationDao = new ConfigurationDatabaseDao(databaseManager);
	}

	@Override
	public void save(MetricConfiguration metricConfiguration, String configurationName) {
		Configuration configuration = getConfiguration(configurationName);
		String metricName = metricConfiguration.getMetric().getName();
		if (configuration.containsMetric(metricName))
			configuration.replaceMetricConfiguration(metricName, metricConfiguration);
		else
			configuration.addMetricConfiguration(metricConfiguration);
		databaseManager.evictFromCache(RangeRecord.class);
		configurationDao.save(configuration);
	}

	@Override
	public MetricConfiguration getMetricConfiguration(String configurationName, String metricName) {
		Configuration configuration = getConfiguration(configurationName);
		return configuration.getConfigurationFor(metricName);
	}

	@Override
	public void removeMetricConfiguration(String configurationName, String metricName) {
		Configuration configuration = getConfiguration(configurationName);
		configuration.removeMetric(metricName);
		configurationDao.save(configuration);
	}

	private Configuration getConfiguration(String configurationName) {
		return configurationDao.getConfiguration(configurationName);
	}
}