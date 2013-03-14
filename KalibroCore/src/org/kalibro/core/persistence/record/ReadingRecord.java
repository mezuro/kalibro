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
@Table(name = "\"reading\"")
public class ReadingRecord extends ReadingDto {

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "reading")
	@TableGenerator(name = "reading", table = "sequences", pkColumnName = "table_name",
		valueColumnName = "sequence_count", pkColumnValue = "reading", initialValue = 1, allocationSize = 1)
	@Column(name = "\"id\"", nullable = false, unique = true)
	private Long id;

	@Column(name = "\"group\"", nullable = false)
	private Long group;

	@Column(name = "\"label\"", nullable = false)
	private String label;

	@Column(name = "\"grade\"", nullable = false)
	private Long grade;

	@Column(name = "\"color\"", nullable = false)
	private Integer color;

	public ReadingRecord() {
		super();
	}

	public ReadingRecord(Reading reading) {
		this(reading, null);
	}

	public ReadingRecord(Reading reading, Long groupId) {
		this.id = reading.getId();
		label = reading.getLabel();
		grade = Double.doubleToLongBits(reading.getGrade());
		color = reading.getColor().getRGB();
		group = groupId;
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