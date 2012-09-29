package org.kalibro.dao;

import java.util.SortedSet;

import org.kalibro.ReadingGroup;

/**
 * Data access object for {@link ReadingGroup}.
 * 
 * @author Carlos Morais
 */
public interface ReadingGroupDao {

	boolean exists(Long groupId);

	ReadingGroup get(Long groupId);

	ReadingGroup readingGroupOf(Long metricConfigurationId);

	SortedSet<ReadingGroup> all();

	Long save(ReadingGroup group);

	void delete(Long groupId);
}