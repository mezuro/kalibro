package org.kalibro.client;

import java.util.Date;

import org.kalibro.KalibroException;
import org.kalibro.core.dao.ProjectResultDao;
import org.kalibro.core.model.ProjectResult;
import org.kalibro.service.ProjectResultEndpoint;

class ProjectResultClientDao extends EndpointClient<ProjectResultEndpoint> implements ProjectResultDao {

	protected ProjectResultClientDao(String serviceAddress) {
		super(serviceAddress, ProjectResultEndpoint.class);
	}

	@Override
	public void save(ProjectResult result) {
		throw new KalibroException("Cannot save project result remotely");
	}

	@Override
	public boolean hasResultsFor(String projectName) {
		return port.hasResultsFor(projectName);
	}

	@Override
	public boolean hasResultsBefore(Date date, String projectName) {
		return port.hasResultsBefore(date, projectName);
	}

	@Override
	public boolean hasResultsAfter(Date date, String projectName) {
		return port.hasResultsAfter(date, projectName);
	}

	@Override
	public ProjectResult getFirstResultOf(String projectName) {
		return port.getFirstResultOf(projectName).convert();
	}

	@Override
	public ProjectResult getLastResultOf(String projectName) {
		return port.getLastResultOf(projectName).convert();
	}

	@Override
	public ProjectResult getLastResultBefore(Date date, String projectName) {
		return port.getLastResultBefore(date, projectName).convert();
	}

	@Override
	public ProjectResult getFirstResultAfter(Date date, String projectName) {
		return port.getFirstResultAfter(date, projectName).convert();
	}
}