package org.kalibro;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.kalibro.core.abstractentity.AbstractEntity;
import org.kalibro.core.abstractentity.IdentityField;
import org.kalibro.core.abstractentity.SortingFields;

/**
 * Processing is automatically loading, analyzing and evaluating source code from a {@link Repository}. The processing
 * contains the state of the processing. If the processing is finished, contains also results or an error.
 * 
 * @author Carlos Morais
 */
@SortingFields({"repository", "date"})
public class Processing extends AbstractEntity<Processing> {

	private Long id;

	@IdentityField
	private Repository repository;

	@IdentityField
	private Date date;

	private ProcessState state;
	private Throwable error;

	private Map<ProcessState, Long> stateTimes;
	private ModuleResult resultsRoot;

	public Processing(Repository repository) {
		setRepository(repository);
		setDate(new Date());
		setState(ProcessState.LOADING);
		stateTimes = new HashMap<ProcessState, Long>();
	}

	public Long getId() {
		return id;
	}

	public Repository getRepository() {
		return repository;
	}

	public void setRepository(Repository repository) {
		this.repository = repository;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public ProcessState getState() {
		if (error != null)
			return ProcessState.ERROR;
		return state;
	}

	public String getStateMessage() {
		return getState().getMessage(repository.getCompleteName());
	}

	public ProcessState getStateWhenErrorOcurred() {
		return (error == null) ? null : state;
	}

	public void setState(ProcessState state) {
		if (state == ProcessState.ERROR)
			throw new KalibroException("Use setError(Throwable) to put repository in error state");
		this.state = state;
		error = null;
	}

	public Throwable getError() {
		return error;
	}

	public void setError(Throwable error) {
		this.error = error;
	}

	public Long getStateTime(ProcessState passedState) {
		return stateTimes.get(passedState);
	}

	public void setStateTime(ProcessState passedState, long time) {
		stateTimes.put(passedState, time);
	}

	public ModuleResult getResultsRoot() {
		return resultsRoot;
	}

	public void setResultsRoot(ModuleResult resultsRoot) {
		this.resultsRoot = resultsRoot;
	}
}