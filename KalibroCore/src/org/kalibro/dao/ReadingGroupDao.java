package org.kalibro.dao;

import java.util.List;

import org.kalibro.ReadingGroup;

/**
 * Data access object for {@link ReadingGroup}.
 * 
 * @author Carlos Morais
 */
public interface ReadingGroupDao {

	List<ReadingGroup> all();

	ReadingGroup get(Long groupId);

	Long save(ReadingGroup group);

	void delete(Long groupId);
}