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
@Table(name = "\"DESCENDANT_RESULT\"")
public class DescendantResultRecord extends DataTransferObject<Double> {

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "\"metric_result\"", nullable = false, referencedColumnName = "\"id\"")
	@SuppressWarnings("unused" /* used by JPA */)
	private MetricResultRecord metricResult;

	@Id
	@GeneratedValue
	@Column(name = "\"id\"", nullable = false)
	@SuppressWarnings("unused" /* used by JPA */)
	private Long id;

	@Column(name = "\"value\"", nullable = false)
	private Long value;

	public DescendantResultRecord() {
		super();
	}

	public DescendantResultRecord(Double value) {
		this(value, null);
	}

	public DescendantResultRecord(Double value, MetricResultRecord metricResultRecord) {
		metricResult = metricResultRecord;
		this.value = Double.doubleToLongBits(value);
	}

	@Override
	public Double convert() {
		return Double.longBitsToDouble(value);
	}
}