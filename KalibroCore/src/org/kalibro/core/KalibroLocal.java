package org.kalibro.core;

import static org.kalibro.Kalibro.*;
import static org.kalibro.core.concurrent.Task.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.kalibro.KalibroFacade;
import org.kalibro.core.command.CommandTask;
import org.kalibro.core.model.enums.RepositoryType;
import org.kalibro.core.persistence.dao.DaoFactory;
import org.kalibro.core.persistence.database.DatabaseDaoFactory;
import org.kalibro.core.processing.ProcessProjectTask;

public class KalibroLocal extends KalibroFacade {

	private static final long REPOSITORY_VALIDATION_TIMEOUT = 1 * MINUTE;

	private Map<String, ProcessProjectTask> processTasks;
	private Map<String, Integer> processPeriods;

	public KalibroLocal() {
		super();
		processTasks = new HashMap<String, ProcessProjectTask>();
		processPeriods = new HashMap<String, Integer>();
	}

	@Override
	protected DaoFactory createDaoFactory() {
		return new DatabaseDaoFactory(currentSettings().getDatabaseSettings());
	}

	@Override
	protected Set<RepositoryType> getSupportedRepositoryTypes() {
		Set<RepositoryType> types = new TreeSet<RepositoryType>();
		for (RepositoryType type : RepositoryType.values())
			if (isSupported(type))
				types.add(type);
		return types;
	}

	private boolean isSupported(RepositoryType type) {
		try {
			validateRepositoryType(type);
			return true;
		} catch (Exception exception) {
			return false;
		}
	}

	private void validateRepositoryType(RepositoryType type) {
		for (String validationCommand : type.getProjectLoader().getValidationCommands())
			new CommandTask(validationCommand).executeAndWait(REPOSITORY_VALIDATION_TIMEOUT);
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