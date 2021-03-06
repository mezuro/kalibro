package org.kalibro.core.persistence.record;

import java.awt.Color;

import javax.persistence.*;

import org.kalibro.Range;
import org.kalibro.Reading;
import org.kalibro.dto.RangeDto;

/**
 * Java Persistence API entity for {@link Range} snapshots.
 * 
 * @author Carlos Morais
 */
@Entity(name = "RangeSnapshot")
@Table(name = "\"range_snapshot\"")
public class RangeSnapshotRecord extends RangeDto {

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "range_snapshot")
	@TableGenerator(name = "range_snapshot", table = "sequences", pkColumnName = "table_name",
		valueColumnName = "sequence_count", pkColumnValue = "range_snapshot", initialValue = 1, allocationSize = 1)
	@Column(name = "\"id\"", nullable = false, unique = true)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "\"configuration_snapshot\"", nullable = false, referencedColumnName = "\"id\"")
	private MetricConfigurationSnapshotRecord configurationSnapshot;

	@Column(name = "\"beginning\"", nullable = false)
	private Long beginning;

	@Column(name = "\"end\"", nullable = false)
	private Long end;

	@Column(name = "\"comments\"")
	private String comments;

	@Column(name = "\"label\"")
	private String label;

	@Column(name = "\"grade\"")
	private Long grade;

	@Column(name = "\"color\"")
	private Integer color;

	public RangeSnapshotRecord() {
		super();
	}

	public RangeSnapshotRecord(Range range) {
		this(range, null);
	}

	public RangeSnapshotRecord(Range range, MetricConfigurationSnapshotRecord configurationSnapshot) {
		this.configurationSnapshot = configurationSnapshot;
		beginning = Double.doubleToLongBits(range.getBeginning());
		end = Double.doubleToLongBits(range.getEnd());
		comments = range.getComments();
		if (range.hasReading())
			setReading(range.getReading());
	}

	private void setReading(Reading reading) {
		label = reading.getLabel();
		grade = Double.doubleToLongBits(reading.getGrade());
		color = reading.getColor().getRGB();
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
		return new Reading(label, Double.longBitsToDouble(grade), new Color(color));
	}

	@Override
	public Long readingId() {
		return label == null ? null : 0L;
	}
}