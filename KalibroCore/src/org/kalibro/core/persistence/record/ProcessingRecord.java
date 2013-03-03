package org.kalibro.core.persistence.record;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.*;

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
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
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

	@Column(name = "\"loading_time\"")
	private Long loadingTime;

	@Column(name = "\"collecting_time\"")
	private Long collectingTime;

	@Column(name = "\"analyzing_time\"")
	private Long analyzingTime;

	@Column(name = "\"results_root\"")
	private Long resultsRoot;

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
		loadingTime = processing.getStateTime(ProcessState.LOADING);
		collectingTime = processing.getStateTime(ProcessState.COLLECTING);
		analyzingTime = processing.getStateTime(ProcessState.ANALYZING);
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
		putTime(map, ProcessState.LOADING, loadingTime);
		putTime(map, ProcessState.COLLECTING, collectingTime);
		putTime(map, ProcessState.ANALYZING, analyzingTime);
		return map;
	}

	private void putTime(Map<ProcessState, Long> map, ProcessState passedState, Long time) {
		if (time != null)
			map.put(passedState, time);
	}

	@Override
	public Long resultsRootId() {
		return resultsRoot;
	}
}