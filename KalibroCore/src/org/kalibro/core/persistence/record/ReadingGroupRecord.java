package org.kalibro.core.persistence.record;

import java.util.Collection;

import javax.persistence.*;

import org.eclipse.persistence.annotations.CascadeOnDelete;
import org.kalibro.ReadingGroup;
import org.kalibro.dto.ReadingGroupDto;

/**
 * Java Persistence API entity for {@link ReadingGroup}.
 * 
 * @author Carlos Morais
 */
@Entity(name = "ReadingGroup")
@Table(name = "\"READING_GROUP\"")
public class ReadingGroupRecord extends ReadingGroupDto {

	@Id
	@GeneratedValue
	@Column(name = "\"id\"", nullable = false)
	private Long id;

	@Column(name = "\"name\"", nullable = false, unique = true)
	private String name;

	@Column(name = "\"description\"")
	private String description;

	@CascadeOnDelete
	@OneToMany(mappedBy = "group", orphanRemoval = true)
	private Collection<ReadingRecord> readings;

	public ReadingGroupRecord() {
		super();
	}

	public ReadingGroupRecord(Long id) {
		this.id = id;
	}

	public ReadingGroupRecord(ReadingGroup readingGroup) {
		this(readingGroup.getId());
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