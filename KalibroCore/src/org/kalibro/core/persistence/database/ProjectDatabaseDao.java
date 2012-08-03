package org.kalibro.core.persistence.database;

import java.util.List;

import org.apache.commons.io.FileUtils;
import org.kalibro.Kalibro;
import org.kalibro.core.model.Project;
import org.kalibro.core.persistence.dao.ProjectDao;
import org.kalibro.core.persistence.database.entities.ProjectRecord;

class ProjectDatabaseDao extends DatabaseDao<Project, ProjectRecord> implements ProjectDao {

	protected ProjectDatabaseDao(DatabaseManager databaseManager) {
		super(databaseManager, ProjectRecord.class);
	}

	@Override
	public void save(Project project) {
		ProjectRecord record = new ProjectRecord(project);
		record = databaseManager.save(record);
		project.setId(record.convert().getId());
	}

	@Override
	public List<String> getProjectNames() {
		return getAllNames();
	}

	@Override
	public boolean hasProject(String projectName) {
		return hasEntity(projectName);
	}

	@Override
	public Project getProject(String projectName) {
		return getByName(projectName);
	}

	@Override
	public void removeProject(String projectName) {
		Project project = getByName(projectName);
		ProjectRecord record = new ProjectRecord(project);
		databaseManager.beginTransaction();
		delete("MetricResult", "module.projectResult.project.name", projectName);
		delete("Module", "projectResult.project.name", projectName);
		delete("ProjectResult", "project.name", projectName);
		databaseManager.remove(record);
		databaseManager.commitTransaction();
		FileUtils.deleteQuietly(Kalibro.currentSettings().getLoadDirectoryFor(project));
	}

	private void delete(String table, String projectNameField, String projectName) {
		String queryText = "DELETE FROM " + table + " t WHERE t." + projectNameField + " = :projectName";
		Query<ProjectRecord> query = createRecordQuery(queryText);
		query.setParameter("projectName", projectName);
		query.executeUpdate();
	}
}