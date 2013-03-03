package org.kalibro.core.persistence.record;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "\"id\"", nullable = false, unique = true)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "\"module_result\"", nullable = false, referencedColumnName = "\"id\"")
	private ModuleResultRecord moduleResult;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "\"configuration\"", nullable = false, referencedColumnName = "\"id\"")
	private MetricConfigurationSnapshotRecord configuration;

	@Column(name = "\"value\"", nullable = false)
	private Long value;

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "\"error\"", referencedColumnName = "\"id\"")
	private ThrowableRecord error;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "metricResult")
	private Collection<DescendantResultRecord> descendantResults;

	public MetricResultRecord() {
		super();
	}

	public MetricResultRecord(MetricResult metricResult) {
		this(metricResult, null);
	}

	public MetricResultRecord(MetricResult metricResult, ModuleResultRecord moduleResult) {
		id = metricResult.getId();
		this.moduleResult = moduleResult;
		configuration = new MetricConfigurationSnapshotRecord(metricResult.getConfiguration().getId());
		value = Double.doubleToLongBits(metricResult.getValue());
		error = metricResult.hasError() ? new ThrowableRecord(metricResult.getError()) : null;
		setDescendantResults(metricResult.getDescendantResults());
	}

	private void setDescendantResults(List<Double> descendantResults) {
		this.descendantResults = new ArrayList<DescendantResultRecord>();
		for (Double descendantResult : descendantResults)
			this.descendantResults.add(new DescendantResultRecord(descendantResult, this));
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