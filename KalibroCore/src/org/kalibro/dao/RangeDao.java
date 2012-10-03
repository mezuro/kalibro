package org.kalibro.dao;

import java.util.SortedSet;

import org.kalibro.Range;

public interface RangeDao {

	SortedSet<Range> rangesOf(Long configurationId);

	Long save(Range range, Long configurationId);

	void delete(Long rangeId);
}