package org.kalibro.service;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.kalibro.service.entities.ProjectXml;
import org.kalibro.service.entities.RawProjectXml;

@WebService(name = "ProjectEndpoint", serviceName = "ProjectEndpointService")
public interface ProjectEndpoint {

	@WebMethod
	void saveProject(@WebParam(name = "project") RawProjectXml project);

	@WebMethod
	@WebResult(name = "projectName")
	List<String> getProjectNames();

	@WebMethod
	@WebResult(name = "hasProject")
	boolean hasProject(@WebParam(name = "projectName") String projectName);

	@WebMethod
	@WebResult(name = "project")
	ProjectXml getProject(@WebParam(name = "projectName") String projectName);

	@WebMethod
	void removeProject(@WebParam(name = "projectName") String projectName);
}