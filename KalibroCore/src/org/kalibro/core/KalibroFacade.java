package org.kalibro.core;

import java.util.Set;

import org.kalibro.core.model.enums.RepositoryType;
import org.kalibro.core.persistence.dao.DaoFactory;

public abstract class KalibroFacade {

	protected DaoFactory daoFactory;

	protected KalibroFacade() {
		daoFactory = createDaoFactory();
	}

	protected DaoFactory getDaoFactory() {
		return daoFactory;
	}

	protected abstract DaoFactory createDaoFactory();

	protected abstract Set<RepositoryType> getSupportedRepositoryTypes();

	protected abstract void processProject(String projectName);

	protected abstract void processPeriodically(String projectName, Integer periodInDays);

	protected abstract Integer getProcessPeriod(String projectName);

	protected abstract void cancelPeriodicProcess(String projectName);
}