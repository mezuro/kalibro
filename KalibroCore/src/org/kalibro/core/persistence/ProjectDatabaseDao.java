package org.kalibro.core.persistence;

import java.util.List;

import javax.persistence.TypedQuery;

import org.apache.commons.io.FileUtils;
import org.kalibro.core.model.Configuration;
import org.kalibro.core.model.Project;
import org.kalibro.core.persistence.record.ProjectRecord;
import org.kalibro.dao.ConfigurationDao;
import org.kalibro.dao.ProjectDao;

class ProjectDatabaseDao extends DatabaseDao<Project, ProjectRecord> implements ProjectDao {

	private ConfigurationDao configurationDao;

	protected ProjectDatabaseDao(RecordManager recordManager) {
		super(recordManager, ProjectRecord.class);
		configurationDao = new ConfigurationDatabaseDao(recordManager);
	}

	@Override
	public void save(Project project) {
		ProjectRecord record = createRecord(project);
		record = recordManager.save(record);
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
		ProjectRecord record = createRecord(project);
		recordManager.beginTransaction();
		delete("MetricResult", "module.projectResult.project.name", projectName);
		delete("Module", "projectResult.project.name", projectName);
		delete("ProjectResult", "project.name", projectName);
		recordManager.remove(record);
		recordManager.commitTransaction();
		FileUtils.deleteQuietly(project.getDirectory());
	}

	private ProjectRecord createRecord(Project project) {
		Configuration configuration = configurationDao.getConfiguration(project.getConfigurationName());
		return new ProjectRecord(project, configuration.getId());
	}

	private void delete(String table, String projectNameField, String projectName) {
		String queryText = "DELETE FROM " + table + " t WHERE t." + projectNameField + " = :projectName";
		TypedQuery<ProjectRecord> query = createRecordQuery(queryText);
		query.setParameter("projectName", projectName);
		query.executeUpdate();
	}
}