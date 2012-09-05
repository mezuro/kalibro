package org.kalibro.core.dao;

import java.util.List;

import org.kalibro.Reading;

/**
 * Data Access Object for {@link Reading}.
 * 
 * @author Carlos Morais
 */
public interface ReadingDao {

	List<Reading> readingsOf(Long groupId);

	void save(Reading reading);

	void delete(Reading reading);
}