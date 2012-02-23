package org.kalibro.service;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.kalibro.service.entities.ProjectXml;
import org.kalibro.service.entities.RawProjectXml;

@WebService
public interface ProjectEndpoint {

	@WebMethod
	void saveProject(RawProjectXml project);

	@WebMethod
	List<String> getProjectNames();

	@WebMethod
	ProjectXml getProject(String projectName);

	@WebMethod
	void removeProject(String projectName);
}