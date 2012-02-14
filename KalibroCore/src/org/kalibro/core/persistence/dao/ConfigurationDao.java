package org.kalibro.core.persistence.dao;

import java.util.List;

import org.kalibro.core.model.Configuration;

public interface ConfigurationDao {

	public void save(Configuration configuration);

	public List<String> getConfigurationNames();

	public Configuration getConfiguration(String configurationName);

	public Configuration getConfigurationFor(String projectName);

	public void removeConfiguration(String configurationName);
}