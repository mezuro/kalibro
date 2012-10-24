package org.kalibro.dto;

import java.util.Date;
import java.util.Map;

import org.kalibro.ModuleResult;
import org.kalibro.ProcessState;
import org.kalibro.Processing;
import org.kalibro.Repository;
import org.kalibro.dao.ModuleResultDao;
import org.kalibro.dao.RepositoryDao;

/**
 * Data transfer object for {@link Processing}.
 * 
 * @author Carlos Morais
 */
public abstract class ProcessingDto extends DataTransferObject<Processing> {

	@Override
	public Processing convert() {
		Processing processing = new Processing(repository());
		setId(processing, id());
		processing.setDate(date());
		convertState(processing);
		convertStateTimes(processing);
		processing.setResultsRoot(resultsRoot());
		return processing;
	}

	private void convertState(Processing processing) {
		processing.setState(state());
		if (error() != null)
			processing.setError(error());
	}

	private void convertStateTimes(Processing processing) {
		Map<ProcessState, Long> stateTimes = stateTimes();
		for (ProcessState state : stateTimes.keySet())
			processing.setStateTime(state, stateTimes.get(state));
	}

	public abstract Long id();

	public Repository repository() {
		return DaoLazyLoader.createProxy(RepositoryDao.class, "repositoryOf", id());
	}

	public abstract Date date();

	public abstract ProcessState state();

	public abstract Throwable error();

	public abstract Map<ProcessState, Long> stateTimes();

	public ModuleResult resultsRoot() {
		return DaoLazyLoader.createProxy(ModuleResultDao.class, "resultsRootOf", id());
	}
}