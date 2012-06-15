package org.kalibro.core.persistence.database;

import java.util.List;

import org.kalibro.core.model.Project;
import org.kalibro.core.persistence.dao.ProjectDao;
import org.kalibro.core.persistence.database.entities.ProjectRecord;

class ProjectDatabaseDao extends DatabaseDao<Project, ProjectRecord> implements ProjectDao {

	protected ProjectDatabaseDao(DatabaseManager databaseManager) {
		super(databaseManager, ProjectRecord.class);
	}

	@Override
	public void save(Project project) {
		databaseManager.save(new ProjectRecord(project));
	}

	@Override
	public List<String> getProjectNames() {
		return getAllNames();
	}

	@Override
	public Project getProject(String projectName) {
		return getByName(projectName);
	}

	@Override
	public void removeProject(String projectName) {
		ProjectRecord record = new ProjectRecord(getByName(projectName));
		databaseManager.beginTransaction();
		delete("MetricResult", "module.projectResult.project.name", projectName);
		delete("Module", "projectResult.project.name", projectName);
		delete("ProjectResult", "project.name", projectName);
		databaseManager.remove(record);
		databaseManager.commitTransaction();
	}

	private void delete(String table, String projectNameField, String projectName) {
		String queryText = "DELETE FROM " + table + " t WHERE t." + projectNameField + " = :projectName";
		Query<ProjectRecord> query = createRecordQuery(queryText);
		query.setParameter("projectName", projectName);
		query.executeUpdate();
	}
}