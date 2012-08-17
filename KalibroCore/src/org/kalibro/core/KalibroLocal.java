package org.kalibro.core;

import static org.kalibro.core.Kalibro.*;
import static org.kalibro.core.concurrent.Task.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.kalibro.core.model.enums.RepositoryType;
import org.kalibro.core.persistence.dao.DaoFactory;
import org.kalibro.core.persistence.database.DatabaseDaoFactory;
import org.kalibro.core.processing.ProcessProjectTask;

public class KalibroLocal extends KalibroFacade {

	private Map<String, ProcessProjectTask> processTasks;
	private Map<String, Integer> processPeriods;

	public KalibroLocal() {
		super();
		processTasks = new HashMap<String, ProcessProjectTask>();
		processPeriods = new HashMap<String, Integer>();
	}

	@Override
	protected DaoFactory createDaoFactory() {
		return new DatabaseDaoFactory(currentSettings().getServerSettings().getDatabaseSettings());
	}

	@Override
	protected Set<RepositoryType> getSupportedRepositoryTypes() {
		return RepositoryType.supportedTypes();
	}

	@Override
	protected void processProject(String projectName) {
		new ProcessProjectTask(projectName).executeInBackground();
	}

	@Override
	protected void processPeriodically(String projectName, Integer periodInDays) {
		cancelPeriodicProcess(projectName);
		ProcessProjectTask task = new ProcessProjectTask(projectName);
		processTasks.put(projectName, task);
		processPeriods.put(projectName, periodInDays);
		task.executePeriodically(periodInDays * DAY);
	}

	@Override
	protected Integer getProcessPeriod(String projectName) {
		return processPeriods.containsKey(projectName) ? processPeriods.get(projectName) : 0;
	}

	@Override
	protected void cancelPeriodicProcess(String projectName) {
		if (processTasks.containsKey(projectName)) {
			processTasks.get(projectName).cancelPeriodicExecution();
			processTasks.remove(projectName);
			processPeriods.remove(projectName);
		}
	}
}