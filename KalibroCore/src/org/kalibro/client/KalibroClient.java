package org.kalibro.client;

import java.util.Set;

import org.kalibro.core.KalibroFacade;
import org.kalibro.core.model.enums.RepositoryType;
import org.kalibro.service.KalibroEndpoint;

public class KalibroClient extends KalibroFacade {

	private KalibroEndpoint port;

	public KalibroClient() {
		super();
		port = EndpointPortFactory.getEndpointPort(KalibroEndpoint.class);
	}

	@Override
	protected Set<RepositoryType> getSupportedRepositoryTypes() {
		return port.getSupportedRepositoryTypes();
	}

	@Override
	protected void processProject(String projectName) {
		port.processProject(projectName);
	}

	@Override
	protected void processPeriodically(String projectName, Integer periodInDays) {
		port.processPeriodically(projectName, periodInDays);
	}

	@Override
	protected Integer getProcessPeriod(String projectName) {
		return port.getProcessPeriod(projectName);
	}

	@Override
	protected void cancelPeriodicProcess(String projectName) {
		port.cancelPeriodicProcess(projectName);
	}
}