package org.kalibro.dao;

import java.util.List;

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

	List<Configuration> all();

	Long save(Configuration configuration);

	void delete(Long configurationId);
}