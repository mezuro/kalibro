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
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "reading_group")
	@TableGenerator(name = "reading_group", table = "sequences", pkColumnName = "table_name",
		valueColumnName = "sequence_count", pkColumnValue = "reading_group", initialValue = 1, allocationSize = 1)
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