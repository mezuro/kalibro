package org.kalibro;

import java.util.Set;

import org.kalibro.core.model.enums.RepositoryType;
import org.kalibro.core.persistence.dao.DaoFactory;

class FacadeStub extends KalibroFacade {

	protected FacadeStub(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	protected DaoFactory createDaoFactory() {
		return daoFactory;
	}

	@Override
	protected Set<RepositoryType> getSupportedRepositoryTypes() {
		return null;
	}

	@Override
	protected void processProject(String projectName) {
		return;
	}

	@Override
	protected void processPeriodically(String projectName, Integer periodInDays) {
		return;
	}

	@Override
	protected Integer getProcessPeriod(String projectName) {
		return 0;
	}

	@Override
	protected void cancelPeriodicProcess(String projectName) {
		return;
	}
}