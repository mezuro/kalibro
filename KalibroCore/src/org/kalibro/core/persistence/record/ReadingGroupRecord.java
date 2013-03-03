package org.kalibro.core.persistence.record;

import javax.persistence.*;

import org.kalibro.ReadingGroup;
import org.kalibro.dto.ReadingGroupDto;

/**
 * Java Persistence API entity for {@link ReadingGroup}.
 * 
 * @author Carlos Morais
 */
@Entity(name = "ReadingGroup")
@Table(name = "\"reading_group\"")
public class ReadingGroupRecord extends ReadingGroupDto {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "\"id\"", nullable = false, unique = true)
	private Long id;

	@Column(name = "\"name\"", nullable = false, unique = true)
	private String name;

	@Column(name = "\"description\"")
	private String description;

	public ReadingGroupRecord() {
		super();
	}

	public ReadingGroupRecord(ReadingGroup readingGroup) {
		id = readingGroup.getId();
		name = readingGroup.getName();
		description = readingGroup.getDescription();
	}

	@Override
	public Long id() {
		return id;
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public String description() {
		return description;
	}
}