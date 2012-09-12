package org.kalibro.core.persistence.record;

import java.awt.Color;

import javax.persistence.*;

import org.kalibro.Reading;
import org.kalibro.dto.ReadingDto;

/**
 * Java Persistence API entity for {@link Reading}.
 * 
 * @author Carlos Morais
 */
@Entity(name = "Reading")
@Table(name = "\"READING\"")
public class ReadingRecord extends ReadingDto {

	@Id
	@GeneratedValue
	@Column(name = "\"id\"", nullable = false)
	private Long id;

	@Column(name = "\"label\"", nullable = false)
	private String label;

	@Column(name = "\"grade\"", nullable = false)
	private Long grade;

	@Column(name = "\"color\"", nullable = false)
	private Integer color;

	@SuppressWarnings("unused" /* JPA */)
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "\"group\"", nullable = false, referencedColumnName = "\"id\"")
	private ReadingGroupRecord group;

	public ReadingRecord() {
		super();
	}

	public ReadingRecord(Reading reading) {
		this(reading, (Long) null);
	}

	public ReadingRecord(Reading reading, Long groupId) {
		this(reading, new ReadingGroupRecord(groupId));
	}

	public ReadingRecord(Reading reading, ReadingGroupRecord group) {
		id = reading.getId();
		label = reading.getLabel();
		grade = Double.doubleToLongBits(reading.getGrade());
		color = reading.getColor().getRGB();
		this.group = group;
	}

	@Override
	public Long id() {
		return id;
	}

	@Override
	public String label() {
		return label;
	}

	@Override
	public Double grade() {
		return Double.longBitsToDouble(grade);
	}

	@Override
	public Color color() {
		return new Color(color);
	}
}