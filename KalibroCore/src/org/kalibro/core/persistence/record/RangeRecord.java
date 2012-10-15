package org.kalibro.core.persistence.record;

import javax.persistence.*;

import org.kalibro.Range;
import org.kalibro.dto.RangeDto;

/**
 * Java Persistence API entity for {@link Range}.
 * 
 * @author Carlos Morais
 */
@Entity(name = "Range")
@Table(name = "\"RANGE\"")
public class RangeRecord extends RangeDto {

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "\"configuration\"", nullable = false, referencedColumnName = "\"id\"")
	@SuppressWarnings("unused" /* used by JPA */)
	private MetricConfigurationRecord configuration;

	@Id
	@GeneratedValue
	@Column(name = "\"id\"", nullable = false)
	private Long id;

	@Column(name = "\"beginning\"", nullable = false)
	private Long beginning;

	@Column(name = "\"end\"", nullable = false)
	private Long end;

	@Column(name = "\"comments\"")
	private String comments;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "\"reading\"", referencedColumnName = "\"id\"")
	@SuppressWarnings("unused" /* used by JPA */)
	private ReadingRecord reading;

	public RangeRecord() {
		super();
	}

	public RangeRecord(Range range) {
		this(range, (Long) null);
	}

	public RangeRecord(Range range, Long configurationId) {
		this(range, new MetricConfigurationRecord(configurationId));
	}

	public RangeRecord(Range range, MetricConfigurationRecord configurationRecord) {
		configuration = configurationRecord;
		id = range.getId();
		beginning = Double.doubleToLongBits(range.getBeginning());
		end = Double.doubleToLongBits(range.getEnd());
		comments = range.getComments();
		reading = range.hasReading() ? new ReadingRecord(range.getReading().getId()) : null;
	}

	@Override
	public Long id() {
		return id;
	}

	@Override
	public Double beginning() {
		return Double.longBitsToDouble(beginning);
	}

	@Override
	public Double end() {
		return Double.longBitsToDouble(end);
	}

	@Override
	public String comments() {
		return comments;
	}
}