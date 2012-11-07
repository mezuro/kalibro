package org.kalibro.dao;

import java.util.SortedSet;

import org.kalibro.Reading;

/**
 * Data access object for {@link Reading}.
 * 
 * @author Carlos Morais
 */
public interface ReadingDao {

	Reading get(Long readingId);

	SortedSet<Reading> readingsOf(Long groupId);

	Long save(Reading reading, Long groupId);

	void delete(Long readingId);
}