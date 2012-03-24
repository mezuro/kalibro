package org.kalibro.core;

import static org.kalibro.Kalibro.*;

import java.util.Set;
import java.util.TreeSet;

import org.kalibro.KalibroFacade;
import org.kalibro.core.command.CommandTask;
import org.kalibro.core.concurrent.Task;
import org.kalibro.core.model.enums.RepositoryType;
import org.kalibro.core.persistence.dao.DaoFactory;
import org.kalibro.core.persistence.database.DatabaseDaoFactory;
import org.kalibro.core.processing.ProcessProjectTask;

public class KalibroLocal extends KalibroFacade {

	public KalibroLocal() {
		super();
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
			new CommandTask(validationCommand).executeAndWait(1000);
	}

	@Override
	protected void processProject(String projectName) {
		new ProcessProjectTask(projectName).executeInBackground();
	}

	@Override
	protected void processPeriodically(final String projectName, Integer periodInDays) {
		new ProcessProjectTask(projectName).executePeriodically(periodInDays * Task.DAY);
	}
}