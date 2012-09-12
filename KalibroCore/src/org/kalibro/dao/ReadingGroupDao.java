package org.kalibro.dao;

import java.util.List;

import org.kalibro.ReadingGroup;

/**
 * Data access object for {@link ReadingGroup}.
 * 
 * @author Carlos Morais
 */
public interface ReadingGroupDao {

	boolean exists(Long groupId);

	ReadingGroup get(Long groupId);

	List<ReadingGroup> all();

	Long save(ReadingGroup group);

	void delete(Long groupId);
}