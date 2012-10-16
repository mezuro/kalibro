package org.kalibro.core.persistence.record;

import java.util.ArrayList;
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
@Table(name = "\"METRIC_RESULT\"")
public class MetricResultRecord extends MetricResultDto {

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "\"module_result\"", nullable = false, referencedColumnName = "\"id\"")
	@SuppressWarnings("unused" /* used by JPA */)
	private ModuleResultRecord moduleResult;

	@Id
	@GeneratedValue
	@Column(name = "\"id\"", nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "\"configuration\"", nullable = false, referencedColumnName = "\"id\"")
	private MetricConfigurationSnapshotRecord configuration;

	@Column(name = "\"value\"", nullable = false)
	private Long value;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "metricResult", orphanRemoval = true)
	private List<DescendantResultRecord> descendantResults;

	public MetricResultRecord() {
		super();
	}

	public MetricResultRecord(MetricResult metricResult) {
		this(metricResult, null);
	}

	public MetricResultRecord(MetricResult metricResult, Long moduleResultId) {
		moduleResult = new ModuleResultRecord(moduleResultId);
		id = metricResult.getId();
		configuration = new MetricConfigurationSnapshotRecord(metricResult.getConfiguration().getId());
		value = Double.doubleToLongBits(metricResult.getValue());
		setDescendantResults(metricResult.getDescendantResults());
	}

	private void setDescendantResults(List<Double> descendantResults) {
		this.descendantResults = new ArrayList<DescendantResultRecord>();
		for (Double descendantResult : descendantResults)
			this.descendantResults.add(new DescendantResultRecord(descendantResult));
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
		return null;
	}
}