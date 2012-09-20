package org.kalibro.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kalibro.Configuration;

public class ConfigurationDaoFake implements ConfigurationDao {

	private Map<String, Configuration> configurations = new HashMap<String, Configuration>();

	@Override
	public void save(Configuration configuration) {
		configurations.put(configuration.getName(), configuration);
	}

	@Override
	public List<String> getConfigurationNames() {
		return new ArrayList<String>(configurations.keySet());
	}

	@Override
	public boolean hasConfiguration(String configurationName) {
		return configurations.containsKey(configurationName);
	}

	@Override
	public Configuration getConfiguration(String configurationName) {
		return configurations.get(configurationName);
	}

	@Override
	public Configuration getConfigurationFor(String projectName) {
		return null;
	}

	@Override
	public void delete(Long configurationId) {
		configurations.remove(configurationId);
	}

	@Override
	public List<Configuration> all() {
		// TODO Auto-generated method stub
		return null;
	}
}