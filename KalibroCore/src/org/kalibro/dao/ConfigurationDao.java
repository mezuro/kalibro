package org.kalibro.dao;

import java.util.SortedSet;

import org.kalibro.Configuration;

/**
 * Data access object for {@link Configuration}.
 * 
 * @author Carlos Morais
 */
public interface ConfigurationDao {

	boolean exists(Long configurationId);

	Configuration get(Long configurationId);

	Configuration configurationOf(Long projectId);

	SortedSet<Configuration> all();

	Long save(Configuration configuration);

	void delete(Long configurationId);
}