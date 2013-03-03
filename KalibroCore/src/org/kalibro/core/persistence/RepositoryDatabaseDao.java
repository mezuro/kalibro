package org.kalibro.core.persistence;

import static java.util.concurrent.TimeUnit.*;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.TypedQuery;

import org.kalibro.Configuration;
import org.kalibro.KalibroException;
import org.kalibro.Repository;
import org.kalibro.RepositoryType;
import org.kalibro.core.Identifier;
import org.kalibro.core.loaders.Loader;
import org.kalibro.core.persistence.record.RepositoryRecord;
import org.kalibro.core.processing.ProcessTask;
import org.kalibro.dao.RepositoryDao;
import org.kalibro.dto.DataTransferObject;

/**
 * Database access implementation for {@link RepositoryDao}.
 * 
 * @author Carlos Morais
 */
class RepositoryDatabaseDao extends DatabaseDao<Repository, RepositoryRecord> implements RepositoryDao {

	private Map<Long, ProcessTask> processTasks;

	RepositoryDatabaseDao() {
		super(RepositoryRecord.class);
		processTasks = new HashMap<Long, ProcessTask>();
	}

	@Override
	public SortedSet<RepositoryType> supportedTypes() {
		SortedSet<RepositoryType> supportedTypes = new TreeSet<RepositoryType>();
		for (RepositoryType type : RepositoryType.values())
			if (isSupported(type))
				supportedTypes.add(type);
		return supportedTypes;
	}

	private boolean isSupported(RepositoryType type) {
		try {
			String typeName = Identifier.fromConstant(type.name()).asClassName();
			String loaderName = "org.kalibro.core.loaders." + typeName + "Loader";
			Loader loader = (Loader) Class.forName(loaderName).newInstance();
			loader.validate();
			return true;
		} catch (Exception exception) {
			return false;
		}
	}

	@Override
	public SortedSet<Repository> repositoriesOf(Long projectId) {
		TypedQuery<RepositoryRecord> query = createRecordQuery("repository.project = :projectId");
		query.setParameter("projectId", projectId);
		return DataTransferObject.toSortedSet(query.getResultList());
	}

	@Override
	public Long save(Repository repository, Long projectId) {
		return save(new RepositoryRecord(repository, projectId)).id();
	}

	@Override
	public void process(Long repositoryId) {
		cancelProcessing(repositoryId);
		Repository repository = get(repositoryId);
		Configuration configuration = repository.getConfiguration();
		if (configuration.getNativeMetrics().isEmpty())
			throw new KalibroException("Could not process repository (" + repository +
				") because its configuration (" + configuration + ") has no native metrics.");
		ProcessTask task = new ProcessTask(repository);
		processTasks.put(repositoryId, task);
		executeTask(task, repository.getProcessPeriod());
	}

	private void executeTask(ProcessTask task, Integer processPeriod) {
		if (processPeriod == null || processPeriod <= 0)
			task.executeInBackground();
		else
			task.executePeriodically(processPeriod, DAYS);
	}

	@Override
	public void cancelProcessing(Long repositoryId) {
		if (processTasks.containsKey(repositoryId)) {
			processTasks.get(repositoryId).cancelExecution();
			processTasks.remove(repositoryId);
		}
	}
}