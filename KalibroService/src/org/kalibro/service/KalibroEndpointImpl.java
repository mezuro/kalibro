package org.kalibro.service;

import java.util.Set;
import java.util.TreeSet;

import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.kalibro.core.Kalibro;
import org.kalibro.core.model.enums.RepositoryType;

@WebService(name = "KalibroEndpoint", serviceName = "KalibroEndpointService")
public class KalibroEndpointImpl implements KalibroEndpoint {

	@Override
	@WebResult(name = "repositoryType")
	public Set<RepositoryType> getSupportedRepositoryTypes() {
		Set<RepositoryType> types = new TreeSet<RepositoryType>();
		for (RepositoryType type : Kalibro.getSupportedRepositoryTypes())
			if (!type.isLocal())
				types.add(type);
		return types;
	}

	@Override
	public void processProject(@WebParam(name = "projectName") String projectName) {
		Kalibro.processProject(projectName);
	}

	@Override
	public void processPeriodically(
		@WebParam(name = "projectName") String projectName,
		@WebParam(name = "periodInDays") Integer periodInDays) {
		Kalibro.processPeriodically(projectName, periodInDays);
	}

	@Override
	@WebResult(name = "period")
	public Integer getProcessPeriod(@WebParam(name = "projectName") String projectName) {
		return Kalibro.getProcessPeriod(projectName);
	}

	@Override
	public void cancelPeriodicProcess(@WebParam(name = "projectName") String projectName) {
		Kalibro.cancelPeriodicProcess(projectName);
	}
}