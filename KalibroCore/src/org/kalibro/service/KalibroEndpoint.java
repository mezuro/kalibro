package org.kalibro.service;

import java.util.Set;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.kalibro.core.model.enums.RepositoryType;

@WebService
public interface KalibroEndpoint {

	@WebMethod
	Set<RepositoryType> getSupportedRepositoryTypes();

	@WebMethod
	void processProject(String projectName);

	@WebMethod
	void processPeriodically(String projectName, Integer periodInDays);
}