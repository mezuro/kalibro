package org.kalibro.service;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.kalibro.dao.ProjectDao;
import org.kalibro.service.xml.ProjectXmlRequest;
import org.kalibro.service.xml.ProjectXmlResponse;

/**
 * End point to make {@link ProjectDao} interface available as Web service.
 * 
 * @author Carlos Morais
 */
@WebService(name = "ProjectEndpoint", serviceName = "ProjectEndpointService")
public interface ProjectEndpoint {

	@WebMethod
	@WebResult(name = "exists")
	boolean projectExists(@WebParam(name = "projectId") Long projectId);

	@WebMethod
	@WebResult(name = "project")
	ProjectXmlResponse getProject(@WebParam(name = "projectId") Long projectId);

	@WebMethod
	@WebResult(name = "project")
	List<ProjectXmlResponse> allProjects();

	@WebMethod
	@WebResult(name = "projectId")
	Long saveProject(@WebParam(name = "project") ProjectXmlRequest project);

	@WebMethod
	void deleteProject(@WebParam(name = "projectId") Long projectId);
}