package org.kalibro.client;

import java.util.Date;

import org.kalibro.KalibroException;
import org.kalibro.RepositoryResult;
import org.kalibro.dao.ProjectResultDao;
import org.kalibro.service.ProjectResultEndpoint;

class ProjectResultClientDao extends EndpointClient<ProjectResultEndpoint> implements ProjectResultDao {

	ProjectResultClientDao(String serviceAddress) {
		super(serviceAddress, ProjectResultEndpoint.class);
	}

	@Override
	public void save(RepositoryResult result) {
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
	public RepositoryResult getFirstResultOf(String projectName) {
		return port.getFirstResultOf(projectName).convert();
	}

	@Override
	public RepositoryResult getLastResultOf(String projectName) {
		return port.getLastResultOf(projectName).convert();
	}

	@Override
	public RepositoryResult getLastResultBefore(Date date, String projectName) {
		return port.getLastResultBefore(date, projectName).convert();
	}

	@Override
	public RepositoryResult getFirstResultAfter(Date date, String projectName) {
		return port.getFirstResultAfter(date, projectName).convert();
	}
}