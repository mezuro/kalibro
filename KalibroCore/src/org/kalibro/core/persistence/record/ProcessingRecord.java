package org.kalibro.core.persistence.record;

import java.util.*;

import javax.persistence.*;

import org.eclipse.persistence.annotations.CascadeOnDelete;
import org.kalibro.ModuleResult;
import org.kalibro.ProcessState;
import org.kalibro.Processing;
import org.kalibro.dto.ProcessingDto;

/**
 * Java Persistence API entity for {@link Processing}.
 * 
 * @author Carlos Morais
 */
@Entity(name = "Processing")
@Table(name = "\"processing\"")
public class ProcessingRecord extends ProcessingDto {

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "processing")
	@TableGenerator(name = "processing", table = "sequences", pkColumnName = "table_name",
		valueColumnName = "sequence_count", pkColumnValue = "processing", initialValue = 1, allocationSize = 1)
	@Column(name = "\"id\"", nullable = false, unique = true)
	private Long id;

	@Column(name = "\"repository\"", nullable = false)
	private Long repository;

	@Column(name = "\"date\"", nullable = false)
	private Long date;

	@Column(name = "\"state\"", nullable = false)
	private String state;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "\"error\"", referencedColumnName = "\"id\"")
	private ThrowableRecord error;

	@Column(name = "\"results_root\"")
	private Long resultsRoot;

	@CascadeOnDelete
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "processing")
	private Collection<ProcessingTimeRecord> processingTimes;

	public ProcessingRecord() {
		super();
	}

	public ProcessingRecord(Processing processing) {
		this(processing, null);
	}

	public ProcessingRecord(Processing processing, Long repositoryId) {
		id = processing.getId();
		repository = repositoryId;
		date = processing.getDate().getTime();
		setState(processing);
		setProcessTimes(processing);
		setResultsRoot(processing.getResultsRoot());
	}

	private void setState(Processing processing) {
		state = processing.getState().name();
		if (state.equals("ERROR")) {
			error = new ThrowableRecord(processing.getError());
			state = processing.getStateWhenErrorOcurred().name();
		}
	}

	private void setProcessTimes(Processing processing) {
		processingTimes = new ArrayList<ProcessingTimeRecord>();
		for (ProcessState timedState : ProcessState.values()) {
			Long time = processing.getStateTime(timedState);
			if (time != null)
				processingTimes.add(new ProcessingTimeRecord(timedState, time, this));
		}
	}

	private void setResultsRoot(ModuleResult resultsRoot) {
		this.resultsRoot = resultsRoot == null ? null : resultsRoot.getId();
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
		for (ProcessingTimeRecord processingTime : processingTimes)
			map.put(processingTime.state(), processingTime.time());
		return map;
	}

	@Override
	public Long resultsRootId() {
		return resultsRoot;
	}
}