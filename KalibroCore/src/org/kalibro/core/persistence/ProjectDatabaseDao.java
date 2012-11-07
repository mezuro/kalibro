package org.kalibro.core.persistence;

import javax.persistence.TypedQuery;

import org.kalibro.Project;
import org.kalibro.core.persistence.record.ProjectRecord;
import org.kalibro.dao.ProjectDao;

/**
 * Database access implementation for {@link ProjectDao}.
 * 
 * @author Carlos Morais
 */
class ProjectDatabaseDao extends DatabaseDao<Project, ProjectRecord> implements ProjectDao {

	ProjectDatabaseDao() {
		super(ProjectRecord.class);
	}

	@Override
	public Project projectOf(Long repositoryId) {
		String from = "Repository repository JOIN repository.project project";
		TypedQuery<ProjectRecord> query = createRecordQuery(from, "repository.id = :repositoryId");
		query.setParameter("repositoryId", repositoryId);
		return query.getSingleResult().convert();
	}

	@Override
	public Long save(Project project) {
		return save(new ProjectRecord(project)).id();
	}
}