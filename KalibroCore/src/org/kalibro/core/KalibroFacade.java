package org.kalibro.core;

import java.util.Set;

import org.kalibro.core.model.enums.RepositoryType;

public interface KalibroFacade {

	Set<RepositoryType> getSupportedRepositoryTypes();

	void processProject(String projectName);

	void processPeriodically(String projectName, Integer periodInDays);

	Integer getProcessPeriod(String projectName);

	void cancelPeriodicProcess(String projectName);
}