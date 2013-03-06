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

	@Column(name = "\"module_result\"", nullable = false)
	private Long moduleResult;

	@Column(name = "\"configuration\"", nullable = false)
	private Long configuration;

	@Column(name = "\"value\"", nullable = false)
	private Long value;

	public DescendantResultRecord() {
		super();
	}

	public DescendantResultRecord(Double value) {
		this(value, null, null);
	}

	public DescendantResultRecord(Double value, Long moduleResult, Long configuration) {
		this.moduleResult = moduleResult;
		this.configuration = configuration;
		this.value = Double.doubleToLongBits(value);
	}

	@Override
	public Double convert() {
		return Double.longBitsToDouble(value);
	}
}