package org.kalibro.dao;

import java.util.List;

import org.kalibro.Configuration;

public interface ConfigurationDao {

	List<Configuration> all();

	void save(Configuration configuration);

	List<String> getConfigurationNames();

	boolean hasConfiguration(String configurationName);

	Configuration getConfiguration(String configurationName);

	Configuration getConfigurationFor(String projectName);

	void delete(Long configurationId);

}