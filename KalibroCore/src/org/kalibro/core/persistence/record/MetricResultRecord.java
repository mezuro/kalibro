package org.kalibro.core.persistence.record;

import javax.persistence.*;

import org.kalibro.MetricConfiguration;
import org.kalibro.MetricResult;
import org.kalibro.dto.MetricResultDto;

/**
 * Java Persistence API entity for {@link MetricResult}.
 * 
 * @author Carlos Morais
 */
@Entity(name = "MetricResult")
@Table(name = "\"metric_result\"")
public class MetricResultRecord extends MetricResultDto {

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "metric_result")
	@TableGenerator(name = "metric_result", table = "sequences", pkColumnName = "table_name",
		valueColumnName = "sequence_count", pkColumnValue = "metric_result", initialValue = 1, allocationSize = 1)
	@Column(name = "\"id\"", nullable = false, unique = true)
	private Long id;

	@Column(name = "\"module_result\"", nullable = false)
	private Long moduleResult;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "\"configuration\"", nullable = false, referencedColumnName = "\"id\"")
	private MetricConfigurationSnapshotRecord configuration;

	@Column(name = "\"value\"", nullable = false)
	private Long value;

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "\"error\"", referencedColumnName = "\"id\"")
	private ThrowableRecord error;

	public MetricResultRecord() {
		super();
	}

	public MetricResultRecord(MetricResult metricResult) {
		this(metricResult, null);
	}

	public MetricResultRecord(MetricResult metricResult, Long moduleResultId) {
		id = metricResult.getId();
		moduleResult = moduleResultId;
		configuration = new MetricConfigurationSnapshotRecord(metricResult.getConfiguration().getId());
		value = Double.doubleToLongBits(metricResult.getValue());
		error = metricResult.hasError() ? new ThrowableRecord(metricResult.getError()) : null;
	}

	@Override
	public Long id() {
		return id;
	}

	@Override
	public MetricConfiguration configuration() {
		return configuration.convert();
	}

	@Override
	public Double value() {
		return Double.longBitsToDouble(value);
	}

	@Override
	public Throwable error() {
		return error == null ? null : error.convert();
	}
}