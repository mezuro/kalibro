package org.kalibro.service;

import java.util.List;

import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.kalibro.Kalibro;
import org.kalibro.core.model.Project;
import org.kalibro.service.entities.ProjectXml;
import org.kalibro.service.entities.RawProjectXml;

@WebService
public class ProjectEndpointImpl implements ProjectEndpoint {

	@Override
	public void saveProject(@WebParam(name = "project") RawProjectXml project) {
		Kalibro.getProjectDao().save(project.convert());
	}

	@Override
	@WebResult(name = "projectName")
	public List<String> getProjectNames() {
		return Kalibro.getProjectDao().getProjectNames();
	}

	@Override
	@WebResult(name = "project")
	public ProjectXml getProject(@WebParam(name = "projectName") String projectName) {
		Project project = Kalibro.getProjectDao().getProject(projectName);
		return new ProjectXml(project);
	}

	@Override
	public void removeProject(@WebParam(name = "projectName") String projectName) {
		Kalibro.getProjectDao().removeProject(projectName);
	}
}