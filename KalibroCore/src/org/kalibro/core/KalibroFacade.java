package org.kalibro.core;

import java.util.Set;

import org.kalibro.core.model.enums.RepositoryType;

public abstract class KalibroFacade {

	protected abstract Set<RepositoryType> getSupportedRepositoryTypes();

	protected abstract void processProject(String projectName);

	protected abstract void processPeriodically(String projectName, Integer periodInDays);

	protected abstract Integer getProcessPeriod(String projectName);

	protected abstract void cancelPeriodicProcess(String projectName);
}