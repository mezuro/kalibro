package org.kalibro.dao;

import java.util.Date;
import java.util.List;
import java.util.SortedMap;
import java.util.SortedSet;

import org.kalibro.Metric;
import org.kalibro.MetricResult;

/**
 * Data access object for {@link MetricResult}.
 * 
 * @author Carlos Morais
 */
public interface MetricResultDao {

	List<Double> descendantResultsOf(Long metricResultId);

	SortedSet<MetricResult> metricResultsOf(Long moduleResultId);

	SortedMap<Date, MetricResult> historyOf(Metric metric, Long repositoryId);
}