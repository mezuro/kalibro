package org.kalibro.core;

import java.util.Set;

import org.kalibro.core.model.enums.RepositoryType;

class DummyFacade extends KalibroFacade {

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
		return null;
	}

	@Override
	protected void cancelPeriodicProcess(String projectName) {
		return;
	}
}