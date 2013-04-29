package org.kalibro.core.persistence.record;

import javax.persistence.*;

import org.kalibro.ProcessingObserver;
import org.kalibro.dto.ProcessingObserverDto;

@Entity(name = "ProcessingObserver")
@Table(name = "\"processing_observer\"")
public class ProcessingObserverRecord extends ProcessingObserverDto {

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "processing_observer")
	@TableGenerator(name = "processing_observer", table = "sequences", pkColumnName = "table_name",
		valueColumnName = "sequence_count", pkColumnValue = "processing_observer", initialValue = 1,
		allocationSize = 1)
	@Column(name = "\"id\"", nullable = false, unique = true)
	private Long id;

	@Column(name = "\"repository\"", nullable = false)
	private Long repository;

	@Column(name = "\"name\"", nullable = false)
	private String name;

	@Column(name = "\"email\"", nullable = false)
	private String email;

	public ProcessingObserverRecord() {
		super();
	}

	public ProcessingObserverRecord(ProcessingObserver processingObserver, Long repositoryId) {
		id = processingObserver.getId();
		this.repository = repositoryId;
		name = processingObserver.getName();
		email = processingObserver.getEmail();
	}

	@Override
	public Long id() {
		return id;
	}

	public Long repository() {
		return repository;
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public String email() {
		return email;
	}

}
