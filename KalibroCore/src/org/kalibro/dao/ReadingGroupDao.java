package org.kalibro.dao;

import java.util.List;

import org.kalibro.ReadingGroup;

/**
 * Data Access Object for {@link ReadingGroup}.
 * 
 * @author Carlos Morais
 */
public interface ReadingGroupDao {

	List<ReadingGroup> all();

	ReadingGroup get(Long groupId);

	void save(ReadingGroup group);

	void delete(Long groupId);
}