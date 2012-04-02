package org.kalibro.client;

import java.util.Set;

import org.kalibro.Kalibro;
import org.kalibro.KalibroFacade;
import org.kalibro.client.dao.PortDaoFactory;
import org.kalibro.core.model.enums.RepositoryType;
import org.kalibro.core.persistence.dao.DaoFactory;
import org.kalibro.service.KalibroEndpoint;

public class KalibroClient extends KalibroFacade {

	private KalibroEndpoint port;
	private ProjectStateTracker stateTracker;

	public KalibroClient() {
		super();
		port = EndpointPortFactory.getEndpointPort(KalibroEndpoint.class);
		stateTracker = new ProjectStateTracker(daoFactory.getProjectDao(), changeSupport);
		stateTracker.executePeriodically(Kalibro.currentSettings().getPollingInterval());
	}

	@Override
	protected DaoFactory createDaoFactory() {
		return new PortDaoFactory();
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

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		stateTracker.cancelPeriodicExecution();
	}
}