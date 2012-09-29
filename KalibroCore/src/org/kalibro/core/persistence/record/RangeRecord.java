package org.kalibro.core.persistence.record;

import java.awt.Color;

import javax.persistence.*;

import org.eclipse.persistence.annotations.PrimaryKey;
import org.kalibro.Range;
import org.kalibro.dto.DataTransferObject;

@Entity(name = "Range")
@Table(name = "\"RANGE\"")
@PrimaryKey(columns = {@Column(name = "configuration"), @Column(name = "metricName"), @Column(name = "beginning")})
public class RangeRecord extends DataTransferObject<Range> {

	@ManyToOne(optional = false)
	@JoinColumns({
		@JoinColumn(name = "configuration", nullable = false, referencedColumnName = "configuration"),
		@JoinColumn(name = "metricName", nullable = false, referencedColumnName = "metricName")})
	@SuppressWarnings("unused" /* used by JPA */)
	private MetricConfigurationRecord metricConfiguration;

	@Column(name = "beginning", nullable = false)
	private Long beginning;

	@Column(name = "\"end\"", nullable = false)
	private Long end;

	@Column(nullable = false)
	private String label;

	@Column(nullable = false)
	private Long grade;

	@Column(nullable = false)
	private Integer color;

	@Column(nullable = false)
	private String comments;

	public RangeRecord() {
		super();
	}

	public RangeRecord(Range range) {
		this(range, null);
	}

	public RangeRecord(Range range, MetricConfigurationRecord metricConfiguration) {
		this.metricConfiguration = metricConfiguration;
		beginning = Double.doubleToLongBits(range.getBeginning());
		end = Double.doubleToLongBits(range.getEnd());
		label = range.getLabel();
		grade = Double.doubleToLongBits(range.getGrade());
		color = range.getColor().getRGB();
		comments = range.getComments();
	}

	@Override
	public Range convert() {
		Range range = new Range();
		range.setBeginning(Double.longBitsToDouble(beginning));
		range.setEnd(Double.longBitsToDouble(end));
		range.setLabel(label);
		range.setGrade(Double.longBitsToDouble(grade));
		range.setColor(new Color(color, true));
		range.setComments(comments);
		return range;
	}
}