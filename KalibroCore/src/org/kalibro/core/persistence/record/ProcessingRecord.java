package org.kalibro.core.persistence.record;

import java.util.*;

import javax.persistence.*;

import org.eclipse.persistence.annotations.PrivateOwned;
import org.kalibro.ProcessState;
import org.kalibro.Processing;
import org.kalibro.dto.ProcessingDto;

/**
 * Java Persistence API entity for {@link Processing}.
 * 
 * @author Carlos Morais
 */
@Entity(name = "Processing")
@Table(name = "\"PROCESSING\"")
public class ProcessingRecord extends ProcessingDto {

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "\"repository\"", nullable = false, referencedColumnName = "\"id\"")
	@SuppressWarnings("unused" /* used by JPA */)
	private RepositoryRecord repository;

	@Id
	@GeneratedValue
	@Column(name = "\"id\"", nullable = false)
	private Long id;

	@Column(name = "\"date\"", nullable = false)
	private Long date;

	@Column(name = "\"state\"", nullable = false)
	private String state;

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "\"error\"", referencedColumnName = "\"id\"")
	private ThrowableRecord error;

	@PrivateOwned
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "processing", orphanRemoval = true)
	private Collection<ProcessTimeRecord> processTimes;

	public ProcessingRecord() {
		super();
	}

	public ProcessingRecord(Long id) {
		this.id = id;
	}

	public ProcessingRecord(Processing processing) {
		this(processing.getId());
		repository = new RepositoryRecord(processing.getRepository().getId());
		date = processing.getDate().getTime();
		setState(processing);
		setProcessTimes(processing);
	}

	private void setState(Processing processing) {
		state = processing.getState().name();
		if (state.equals("ERROR")) {
			error = new ThrowableRecord(processing.getError());
			state = processing.getStateWhenErrorOcurred().name();
		}
	}

	private void setProcessTimes(Processing processing) {
		processTimes = new ArrayList<ProcessTimeRecord>();
		for (ProcessState passedState : ProcessState.values()) {
			Long time = processing.getStateTime(passedState);
			if (time != null)
				processTimes.add(new ProcessTimeRecord(passedState, time));
		}
	}

	@Override
	public Long id() {
		return id;
	}

	@Override
	public Date date() {
		return new Date(date);
	}

	@Override
	public ProcessState state() {
		return ProcessState.valueOf(state);
	}

	@Override
	public Throwable error() {
		return error == null ? null : error.convert();
	}

	@Override
	public Map<ProcessState, Long> stateTimes() {
		Map<ProcessState, Long> map = new HashMap<ProcessState, Long>();
		for (ProcessTimeRecord processTime : processTimes)
			map.put(processTime.state(), processTime.time());
		return map;
	}
}