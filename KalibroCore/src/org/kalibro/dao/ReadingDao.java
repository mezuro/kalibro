package org.kalibro.dao;

import java.util.List;

import org.kalibro.Reading;

/**
 * Data access object for {@link Reading}.
 * 
 * @author Carlos Morais
 */
public interface ReadingDao {

	List<Reading> readingsOf(Long groupId);

	Long save(Reading reading);

	void delete(Long readingId);
}