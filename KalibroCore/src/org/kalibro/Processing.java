package org.kalibro;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.kalibro.core.abstractentity.AbstractEntity;
import org.kalibro.core.abstractentity.IdentityField;
import org.kalibro.core.abstractentity.SortingFields;
import org.kalibro.dao.DaoFactory;
import org.kalibro.dao.ProcessingDao;

/**
 * Processing is automatically loading, analyzing and evaluating source code from a {@link Repository}. The processing
 * contains the state of the processing. If the processing is finished, contains also results or an error.
 * 
 * @author Carlos Morais
 */
@SortingFields({"repository", "date"})
public class Processing extends AbstractEntity<Processing> {

	public static boolean hasProcessing(Repository repository) {
		repository.assertSaved();
		return dao().hasProcessing(repository.getId());
	}

	public static boolean hasReadyProcessing(Repository repository) {
		repository.assertSaved();
		return dao().hasReadyProcessing(repository.getId());
	}

	public static boolean hasProcessingAfter(Date date, Repository repository) {
		repository.assertSaved();
		return dao().hasProcessingAfter(date, repository.getId());
	}

	public static boolean hasProcessingBefore(Date date, Repository repository) {
		repository.assertSaved();
		return dao().hasProcessingBefore(date, repository.getId());
	}

	public static ProcessState lastProcessingState(Repository repository) {
		repository.assertSaved();
		return dao().lastProcessingState(repository.getId());
	}

	public static Processing lastReadyProcessing(Repository repository) {
		repository.assertSaved();
		return dao().lastReadyProcessing(repository.getId());
	}

	public static Processing firstProcessing(Repository repository) {
		repository.assertSaved();
		return dao().firstProcessing(repository.getId());
	}

	public static Processing lastProcessing(Repository repository) {
		repository.assertSaved();
		return dao().lastProcessing(repository.getId());
	}

	public static Processing firstProcessingAfter(Date date, Repository repository) {
		repository.assertSaved();
		return dao().firstProcessingAfter(date, repository.getId());
	}

	public static Processing lastProcessingBefore(Date date, Repository repository) {
		repository.assertSaved();
		return dao().lastProcessingBefore(date, repository.getId());
	}

	private static ProcessingDao dao() {
		return DaoFactory.getProcessingDao();
	}

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
		this(repository, new Date());
	}

	public Processing(Repository repository, Date date) {
		this.repository = repository;
		this.date = date;
		this.stateTimes = new HashMap<ProcessState, Long>();
		setState(ProcessState.LOADING);
	}

	public Long getId() {
		return id;
	}

	public Repository getRepository() {
		return repository;
	}

	public Date getDate() {
		return date;
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