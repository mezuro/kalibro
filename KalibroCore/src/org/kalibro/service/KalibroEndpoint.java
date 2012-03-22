package org.kalibro.service;

import java.util.Set;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.kalibro.core.model.enums.RepositoryType;

@WebService
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
}