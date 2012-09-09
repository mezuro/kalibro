package org.kalibro.client;

import java.util.Set;

import org.kalibro.KalibroSettings;
import org.kalibro.core.KalibroFacade;
import org.kalibro.core.model.enums.RepositoryType;
import org.kalibro.service.KalibroEndpoint;

public class KalibroClient extends EndpointClient<KalibroEndpoint> implements KalibroFacade {

	public KalibroClient() {
		super(KalibroSettings.load().getClientSettings().getServiceAddress(), KalibroEndpoint.class);
	}

	@Override
	public Set<RepositoryType> getSupportedRepositoryTypes() {
		return port.getSupportedRepositoryTypes();
	}

	@Override
	public void processProject(String projectName) {
		port.processProject(projectName);
	}

	@Override
	public void processPeriodically(String projectName, Integer periodInDays) {
		port.processPeriodically(projectName, periodInDays);
	}

	@Override
	public Integer getProcessPeriod(String projectName) {
		return port.getProcessPeriod(projectName);
	}

	@Override
	public void cancelPeriodicProcess(String projectName) {
		port.cancelPeriodicProcess(projectName);
	}
}