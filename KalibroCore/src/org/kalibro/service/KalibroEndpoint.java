package org.kalibro.service;

import java.util.Set;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.kalibro.core.model.enums.RepositoryType;

@WebService
public interface KalibroEndpoint {

	@WebMethod
	public Set<RepositoryType> getSupportedRepositoryTypes();

	@WebMethod
	public void processProject(String projectName);

	@WebMethod
	public void processPeriodically(String projectName, Integer periodInDays);
}