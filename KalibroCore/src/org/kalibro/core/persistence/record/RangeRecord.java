package org.kalibro.core.persistence.record;

import javax.persistence.*;

import org.kalibro.Range;
import org.kalibro.Reading;
import org.kalibro.dao.ReadingDao;
import org.kalibro.dto.DaoLazyLoader;
import org.kalibro.dto.RangeDto;

/**
 * Java Persistence API entity for {@link Range}.
 * 
 * @author Carlos Morais
 */
@Entity(name = "Range")
@Table(name = "\"RANGE\"")
public class RangeRecord extends RangeDto {

	@SuppressWarnings("unused" /* used by JPA */)
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "\"configuration\"", nullable = false, referencedColumnName = "\"id\"")
	private MetricConfigurationRecord configuration;

	@Id
	@GeneratedValue
	@Column(name = "\"id\"", nullable = false)
	private Long id;

	@Column(name = "\"beginning\"", nullable = false)
	private Long beginning;

	@Column(name = "\"end\"", nullable = false)
	private Long end;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "\"reading\"", referencedColumnName = "\"id\"")
	private ReadingRecord reading;

	@Column(name = "\"comments\"")
	private String comments;

	public RangeRecord() {
		super();
	}

	public RangeRecord(Range range) {
		this(range, null);
	}

	public RangeRecord(Range range, Long configurationId) {
		configuration = new MetricConfigurationRecord(configurationId);
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

	@Override
	public Reading reading() {
		return reading == null ? null : (Reading) DaoLazyLoader.createProxy(ReadingDao.class, "readingOf", id);
	}
}