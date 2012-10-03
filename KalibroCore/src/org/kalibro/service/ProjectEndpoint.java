package org.kalibro.service;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.kalibro.service.xml.ProjectXmlRequest;
import org.kalibro.service.xml.ProjectXmlResponse;

@WebService(name = "ProjectEndpoint", serviceName = "ProjectEndpointService")
public interface ProjectEndpoint {

	@WebMethod
	void saveProject(@WebParam(name = "project") ProjectXmlRequest project);

	@WebMethod
	@WebResult(name = "projectName")
	List<String> getProjectNames();

	@WebMethod
	@WebResult(name = "hasProject")
	boolean hasProject(@WebParam(name = "projectName") String projectName);

	@WebMethod
	@WebResult(name = "project")
	ProjectXmlResponse getProject(@WebParam(name = "projectName") String projectName);

	@WebMethod
	void removeProject(@WebParam(name = "projectName") String projectName);

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