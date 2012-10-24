package org.kalibro.core.persistence;

import org.kalibro.Project;
import org.kalibro.core.persistence.record.ProjectRecord;
import org.kalibro.dao.ProjectDao;

/**
 * Database access implementation for {@link ProjectDao}.
 * 
 * @author Carlos Morais
 */
class ProjectDatabaseDao extends DatabaseDao<Project, ProjectRecord> implements ProjectDao {

	ProjectDatabaseDao(RecordManager recordManager) {
		super(recordManager, ProjectRecord.class);
	}

	@Override
	public Long save(Project project) {
		return save(new ProjectRecord(project)).id();
	}
}