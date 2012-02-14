package org.kalibro.service;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.kalibro.service.entities.ProjectXml;
import org.kalibro.service.entities.RawProjectXml;

@WebService
public interface ProjectEndpoint {

	@WebMethod
	public void saveProject(RawProjectXml project);

	@WebMethod
	public List<String> getProjectNames();

	@WebMethod
	public ProjectXml getProject(String projectName);

	@WebMethod
	public void removeProject(String projectName);
}