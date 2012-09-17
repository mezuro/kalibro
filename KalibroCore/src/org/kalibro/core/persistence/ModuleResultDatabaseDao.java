package org.kalibro.core.persistence;

import java.util.Date;
import java.util.List;

import javax.persistence.TypedQuery;

import org.kalibro.core.model.Configuration;
import org.kalibro.core.model.MetricResult;
import org.kalibro.core.model.ModuleResult;
import org.kalibro.core.model.ProjectResult;
import org.kalibro.core.persistence.record.MetricResultRecord;
import org.kalibro.dao.ModuleResultDao;

public class ModuleResultDatabaseDao extends DatabaseDao<MetricResult, MetricResultRecord> implements ModuleResultDao {

	private static final String QUERY = "SELECT result FROM MetricResult result " +
		"WHERE result.module.projectResult.project.name = :projectName AND result.module.name = :moduleName";

	protected ModuleResultDatabaseDao(RecordManager recordManager) {
		super(recordManager, MetricResultRecord.class);
	}

	public void save(ModuleResult moduleResult, ProjectResult projectResult) {
		recordManager.saveAll(MetricResultRecord.createRecords(moduleResult, projectResult));
	}

	@Override
	public ModuleResult getModuleResult(String projectName, String moduleName, Date date) {
		TypedQuery<MetricResultRecord> query =
			createRecordQuery(QUERY + " AND result.module.projectResult.date = :date");
		query.setParameter("projectName", projectName);
		query.setParameter("moduleName", moduleName);
		query.setParameter("date", date.getTime());
		ModuleResult moduleResult = MetricResultRecord.convertIntoModuleResults(query.getResultList()).get(0);
		moduleResult.setConfiguration(getConfigurationFor(projectName));
		return moduleResult;
	}

	@Override
	public List<ModuleResult> getResultHistory(String projectName, String moduleName) {
		TypedQuery<MetricResultRecord> query = createRecordQuery(QUERY + " ORDER BY result.module.projectResult.date");
		query.setParameter("projectName", projectName);
		query.setParameter("moduleName", moduleName);
		List<ModuleResult> resultHistory = MetricResultRecord.convertIntoModuleResults(query.getResultList());
		Configuration configuration = getConfigurationFor(projectName);
		for (ModuleResult moduleResult : resultHistory)
			moduleResult.setConfiguration(configuration);
		return resultHistory;
	}

	private Configuration getConfigurationFor(String projectName) {
		return new ConfigurationDatabaseDao(recordManager).getConfigurationFor(projectName);
	}
}