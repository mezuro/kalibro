package org.kalibro.dto;

import java.util.SortedSet;

import org.kalibro.Project;
import org.kalibro.Repository;
import org.kalibro.dao.RepositoryDao;

/**
 * Data transfer object for {@link Project}.
 * 
 * @author Carlos Morais
 */

public abstract class ProjectDto extends DataTransferObject<Project> {

	@Override
	public Project convert() {
		Project project = new Project(name());
		setId(project, id());
		project.setDescription(description() == null ? "" : description());
		project.setRepositories(repositories());
		return project;
	}

	public abstract Long id();

	public abstract String name();

	public abstract String description();

	protected SortedSet<Repository> repositories() {
		return DaoLazyLoader.createProxy(RepositoryDao.class, "repositoriesOf", id());
	}
}