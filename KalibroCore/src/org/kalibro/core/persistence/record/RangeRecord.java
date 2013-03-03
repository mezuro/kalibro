package org.kalibro.core.persistence.record;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.kalibro.Range;
import org.kalibro.dto.RangeDto;

/**
 * Java Persistence API entity for {@link Range}.
 * 
 * @author Carlos Morais
 */
@Entity(name = "Range")
@Table(name = "\"range\"")
public class RangeRecord extends RangeDto {

	@Id
	@Column(name = "\"id\"", nullable = false, unique = true)
	private Long id;

	@Column(name = "\"configuration\"", nullable = false)
	private Long configuration;

	@Column(name = "\"beginning\"", nullable = false)
	private Long beginning;

	@Column(name = "\"end\"", nullable = false)
	private Long end;

	@Column(name = "\"reading\"")
	private Long reading;

	@Column(name = "\"comments\"")
	private String comments;

	public RangeRecord() {
		super();
	}

	public RangeRecord(Range range) {
		this(range, null);
	}

	public RangeRecord(Range range, Long configurationId) {
		id = range.getId();
		configuration = configurationId;
		beginning = Double.doubleToLongBits(range.getBeginning());
		end = Double.doubleToLongBits(range.getEnd());
		reading = range.hasReading() ? range.getReading().getId() : null;
		comments = range.getComments();
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
	public Long readingId() {
		return reading;
	}

	@Override
	public String comments() {
		return comments;
	}
}