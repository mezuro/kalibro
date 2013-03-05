package org.kalibro.core.persistence.record;

import javax.persistence.*;

import org.kalibro.MetricResult;
import org.kalibro.dto.DataTransferObject;

/**
 * Java Persistence API entity for {@link MetricResult} descendant results.
 * 
 * @author Carlos Morais
 */
@Entity(name = "DescendantResult")
@Table(name = "\"descendant_result\"")
public class DescendantResultRecord extends DataTransferObject<Double> {

	@Id
	@GeneratedValue
	@Column(name = "\"id\"", nullable = false, unique = true)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "\"metric_result\"", nullable = false, referencedColumnName = "\"id\"")
	private MetricResultRecord metricResult;

	@Column(name = "\"value\"", nullable = false)
	private Long value;

	public DescendantResultRecord() {
		super();
	}

	public DescendantResultRecord(Double value) {
		this(value, null);
	}

	public DescendantResultRecord(Double value, MetricResultRecord metricResult) {
		this.metricResult = metricResult;
		this.value = Double.doubleToLongBits(value);
	}

	@Override
	public Double convert() {
		return Double.longBitsToDouble(value);
	}
}