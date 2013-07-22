package org.kalibro.dto;

import org.kalibro.Configuration;
import org.kalibro.Project;
import org.kalibro.Repository;
import org.kalibro.RepositoryType;
import org.kalibro.dao.ConfigurationDao;
import org.kalibro.dao.ProjectDao;

/**
 * Data transfer object for {@link Repository}.
 * 
 * @author Carlos Morais
 */
public abstract class RepositoryDto extends DataTransferObject<Repository> {

	@Override
	public Repository convert() {
		Repository repository = new Repository(name(), type(), address());
		setId(repository, id());
		repository.setDescription(description() == null ? "" : description());
		repository.setLicense(license() == null ? "" : license());
		repository.setProcessPeriod(processPeriod());
		repository.setProcessHistorically(processHistorically());
		repository.setConfiguration(configuration());
		set(repository, "project", project());
		return repository;
	}

	public abstract Long id();

	public abstract String name();

	public abstract RepositoryType type();

	public abstract String address();

	public abstract String description();

	public abstract String license();

	public abstract Integer processPeriod();

	public abstract boolean processHistorically();

	private Configuration configuration() {
		return DaoLazyLoader.createProxy(ConfigurationDao.class, "get", configurationId());
	}

	public abstract Long configurationId();

	private Project project() {
		Long projectId = projectId();
		return projectId == null ? null : (Project) DaoLazyLoader.createProxy(ProjectDao.class, "get", projectId);
	}

	protected Long projectId() {
		return null;
	}
}