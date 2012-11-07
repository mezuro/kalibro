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

	SortedSet<ReadingGroup> all();

	Long save(ReadingGroup group);

	void delete(Long groupId);
}