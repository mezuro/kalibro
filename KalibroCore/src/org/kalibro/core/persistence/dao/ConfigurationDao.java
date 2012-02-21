package org.kalibro.core.persistence.dao;

import java.util.List;

import org.kalibro.core.model.Configuration;

public interface ConfigurationDao {

	void save(Configuration configuration);

	List<String> getConfigurationNames();

	Configuration getConfiguration(String configurationName);

	Configuration getConfigurationFor(String projectName);

	void removeConfiguration(String configurationName);
}