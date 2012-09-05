package org.kalibro.core.dao;

import java.util.List;

import org.kalibro.ReadingGroup;

public interface ReadingGroupDao {

	List<ReadingGroup> all();

	void save(ReadingGroup group);

	void delete(ReadingGroup group);
}