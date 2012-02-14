package org.kalibro.service;

import java.util.Date;

import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.kalibro.Kalibro;
import org.kalibro.core.model.ProjectResult;
import org.kalibro.service.entities.ProjectResultXml;

@WebService
public class ProjectResultEndpointImpl implements ProjectResultEndpoint {

	@Override
	@WebResult(name = "hasResults")
	public boolean hasResultsFor(@WebParam(name = "projectName") String projectName) {
		return Kalibro.getProjectResultDao().hasResultsFor(projectName);
	}

	@Override
	@WebResult(name = "hasResults")
	public boolean hasResultsBefore(
		@WebParam(name = "date") Date date,
		@WebParam(name = "projectName") String projectName) {
		return Kalibro.getProjectResultDao().hasResultsBefore(date, projectName);
	}

	@Override
	@WebResult(name = "hasResults")
	public boolean hasResultsAfter(
		@WebParam(name = "date") Date date,
		@WebParam(name = "projectName") String projectName) {
		return Kalibro.getProjectResultDao().hasResultsAfter(date, projectName);
	}

	@Override
	@WebResult(name = "projectResult")
	public ProjectResultXml getFirstResultOf(@WebParam(name = "projectName") String projectName) {
		ProjectResult firstResult = Kalibro.getProjectResultDao().getFirstResultOf(projectName);
		return new ProjectResultXml(firstResult);
	}

	@Override
	@WebResult(name = "projectResult")
	public ProjectResultXml getLastResultOf(@WebParam(name = "projectName") String projectName) {
		ProjectResult lastResult = Kalibro.getProjectResultDao().getLastResultOf(projectName);
		return new ProjectResultXml(lastResult);
	}

	@Override
	@WebResult(name = "projectResult")
	public ProjectResultXml getLastResultBefore(
		@WebParam(name = "date") Date date,
		@WebParam(name = "projectName") String projectName) {
		ProjectResult lastResult = Kalibro.getProjectResultDao().getLastResultBefore(date, projectName);
		return new ProjectResultXml(lastResult);
	}

	@Override
	@WebResult(name = "projectResult")
	public ProjectResultXml getFirstResultAfter(
		@WebParam(name = "date") Date date,
		@WebParam(name = "projectName") String projectName) {
		ProjectResult firstResult = Kalibro.getProjectResultDao().getFirstResultAfter(date, projectName);
		return new ProjectResultXml(firstResult);
	}
}