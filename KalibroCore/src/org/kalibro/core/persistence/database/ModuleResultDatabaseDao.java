package org.kalibro.core.persistence.database;

import java.util.Date;
import java.util.List;

import org.kalibro.core.model.Configuration;
import org.kalibro.core.model.MetricResult;
import org.kalibro.core.model.ModuleResult;
import org.kalibro.core.model.ProjectResult;
import org.kalibro.core.persistence.dao.ModuleResultDao;
import org.kalibro.core.persistence.database.entities.MetricResultRecord;

public class ModuleResultDatabaseDao extends DatabaseDao<MetricResult, MetricResultRecord> implements ModuleResultDao {

	private static final String QUERY = "SELECT result FROM MetricResult result " +
		"WHERE result.module.projectResult.project.name = :projectName AND result.module.name = :moduleName";

	protected ModuleResultDatabaseDao(DatabaseManager databaseManager) {
		super(databaseManager, MetricResultRecord.class);
	}

	public void save(ModuleResult moduleResult, ProjectResult projectResult) {
		databaseManager.save(MetricResultRecord.createRecords(moduleResult, projectResult));
	}

	@Override
	public ModuleResult getModuleResult(String projectName, String moduleName, Date date) {
		Query<MetricResultRecord> query = createRecordQuery(QUERY + " AND result.module.projectResult.date = :date");
		query.setParameter("projectName", projectName);
		query.setParameter("moduleName", moduleName);
		query.setParameter("date", date.getTime());
		ModuleResult moduleResult = MetricResultRecord.convertIntoModuleResults(query.getResultList()).get(0);
		moduleResult.setConfiguration(getConfigurationFor(projectName));
		return moduleResult;
	}

	@Override
	public List<ModuleResult> getResultHistory(String projectName, String moduleName) {
		Query<MetricResultRecord> query = createRecordQuery(QUERY + " ORDER BY result.module.projectResult.date");
		query.setParameter("projectName", projectName);
		query.setParameter("moduleName", moduleName);
		List<ModuleResult> resultHistory = MetricResultRecord.convertIntoModuleResults(query.getResultList());
		Configuration configuration = getConfigurationFor(projectName);
		for (ModuleResult moduleResult : resultHistory)
			moduleResult.setConfiguration(configuration);
		return resultHistory;
	}

	private Configuration getConfigurationFor(String projectName) {
		return new ConfigurationDatabaseDao(databaseManager).getConfigurationFor(projectName);
	}
}