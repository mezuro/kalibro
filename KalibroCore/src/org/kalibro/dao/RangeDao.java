package org.kalibro.dao;

import java.util.SortedSet;

import org.kalibro.Range;

/**
 * Data access object for {@link Range}.
 * 
 * @author Carlos Morais
 */
public interface RangeDao {

	SortedSet<Range> rangesOf(Long metricConfigurationId);

	Long save(Range range, Long metricConfigurationId);

	void delete(Long rangeId);
}