package org.kalibro.service;

import java.util.Set;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.kalibro.core.model.enums.RepositoryType;

@WebService(name = "KalibroEndpoint", serviceName = "KalibroEndpointService")
public interface KalibroEndpoint {

	@WebMethod
	@WebResult(name = "repositoryType")
	Set<RepositoryType> getSupportedRepositoryTypes();

	@WebMethod
	void processProject(@WebParam(name = "projectName") String projectName);

	@WebMethod
	void processPeriodically(
		@WebParam(name = "projectName") String projectName,
		@WebParam(name = "periodInDays") Integer periodInDays);

	@WebMethod
	@WebResult(name = "period")
	Integer getProcessPeriod(@WebParam(name = "projectName") String projectName);

	@WebMethod
	void cancelPeriodicProcess(@WebParam(name = "projectName") String projectName);
}