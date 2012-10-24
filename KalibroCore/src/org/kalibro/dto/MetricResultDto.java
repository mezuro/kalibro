package org.kalibro.dto;

import java.util.List;

import org.kalibro.MetricConfiguration;
import org.kalibro.MetricResult;
import org.kalibro.dao.MetricResultDao;

/**
 * Data transfer object for {@link MetricResult}.
 * 
 * @author Carlos Morais
 */
public abstract class MetricResultDto extends DataTransferObject<MetricResult> {

	@Override
	public MetricResult convert() {
		MetricResult metricResult = error() == null ? convertNormal() : convertWithError();
		setId(metricResult, id());
		metricResult.setDescendantResults(descendantResults());
		return metricResult;
	}

	private MetricResult convertNormal() {
		return new MetricResult(configuration(), value());
	}

	private MetricResult convertWithError() {
		return new MetricResult(configuration(), error());
	}

	public abstract Long id();

	public abstract MetricConfiguration configuration();

	public abstract Double value();

	public abstract Throwable error();

	public List<Double> descendantResults() {
		return DaoLazyLoader.createProxy(MetricResultDao.class, "descendantResultsOf", id());
	}
}