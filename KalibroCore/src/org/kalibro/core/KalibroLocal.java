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
		ProcessProjectTask processTask = getProcessTask(projectName);
		processTask.cancelPeriodicExecution();
		processTask.executePeriodically(periodInDays * DAY);
		processPeriods.put(projectName, periodInDays);
	}

	@Override
	protected Integer getProcessPeriod(String projectName) {
		return processPeriods.containsKey(projectName) ? processPeriods.get(projectName) : 0;
	}

	@Override
	protected void cancelPeriodicProcess(String projectName) {
		getProcessTask(projectName).cancelPeriodicExecution();
	}

	private ProcessProjectTask getProcessTask(String projectName) {
		if (!processTasks.containsKey(projectName))
			processTasks.put(projectName, new ProcessProjectTask(projectName));
		return processTasks.get(projectName);
	}
}