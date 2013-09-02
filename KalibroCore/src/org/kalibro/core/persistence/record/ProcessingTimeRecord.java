package org.kalibro.core.persistence.record;

import javax.persistence.*;

import org.kalibro.ProcessState;

/**
 * Java Persistence API entity for mapping {@link ProcessState} to times.
 * 
 * @author Carlos Morais
 */
@Entity(name = "ProcessingTime")
@Table(name = "\"processing_time\"")
public class ProcessingTimeRecord {

	@Id
	@ManyToOne(optional = false)
	@JoinColumn(name = "\"processing\"", nullable = false, referencedColumnName = "\"id\"")
	private ProcessingRecord processing;

	@Id
	@Column(name = "\"state\"", nullable = false)
	private String state;

	@Column(name = "\"time\"", nullable = false)
	private Long time;

	public ProcessingTimeRecord() {
		super();
	}

	public ProcessingTimeRecord(ProcessState state, Long time, ProcessingRecord processing) {
		this.processing = processing;
		this.time = time;
		this.state = state.name();
	}

	public ProcessState state() {
		return ProcessState.valueOf(state);
	}

	public Long time() {
		return time;
	}
}