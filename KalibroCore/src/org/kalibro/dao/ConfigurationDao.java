package org.kalibro.dao;

import java.util.List;

import org.kalibro.Configuration;

public interface ConfigurationDao {

	void save(Configuration configuration);

	List<String> getConfigurationNames();

	boolean hasConfiguration(String configurationName);

	Configuration getConfiguration(String configurationName);

	Configuration getConfigurationFor(String projectName);

	void removeConfiguration(String configurationName);
}