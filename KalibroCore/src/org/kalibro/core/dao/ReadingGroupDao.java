package org.kalibro.core.dao;

import java.util.List;

import org.kalibro.ReadingGroup;

/**
 * Data Access Object for {@link ReadingGroup}.
 * 
 * @author Carlos Morais
 */
public interface ReadingGroupDao {

	List<ReadingGroup> all();

	void save(ReadingGroup group);

	void delete(ReadingGroup group);
}