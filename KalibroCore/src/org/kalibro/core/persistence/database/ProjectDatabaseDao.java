package org.kalibro.core.persistence.database;

import java.util.List;

import org.kalibro.core.model.Project;
import org.kalibro.core.persistence.dao.ProjectDao;
import org.kalibro.core.persistence.database.entities.ProjectRecord;

public class ProjectDatabaseDao extends DatabaseDao<Project, ProjectRecord> implements ProjectDao {

	public ProjectDatabaseDao(DatabaseManager databaseManager) {
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
		databaseManager.delete(record, new BeforeRemove(projectName));
	}

	private class BeforeRemove implements Runnable {

		private String projectName;

		private BeforeRemove(String projectName) {
			this.projectName = projectName;
		}

		@Override
		public void run() {
			delete("MetricResult", "module.projectResult.project.name");
			delete("Module", "projectResult.project.name");
			delete("ProjectResult", "project.name");
		}

		private void delete(String table, String projectNameField) {
			String queryText = "DELETE FROM " + table + " t WHERE t." + projectNameField + " = :projectName";
			Query<ProjectRecord> query = createRecordQuery(queryText);
			query.setParameter("projectName", projectName);
			query.executeUpdate();
		}
	}
}