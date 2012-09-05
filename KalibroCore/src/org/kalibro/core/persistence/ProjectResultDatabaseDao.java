package org.kalibro.core.persistence;

import java.util.Date;

import org.kalibro.core.dao.ProjectResultDao;
import org.kalibro.core.model.ProjectResult;
import org.kalibro.core.persistence.entities.ProjectResultRecord;

class ProjectResultDatabaseDao extends DatabaseDao<ProjectResult, ProjectResultRecord> implements ProjectResultDao {

	protected ProjectResultDatabaseDao(DatabaseManager databaseManager) {
		super(databaseManager, ProjectResultRecord.class);
	}

	@Override
	public void save(ProjectResult projectResult) {
		databaseManager.save(new ProjectResultRecord(projectResult));
	}

	@Override
	public boolean hasResultsFor(String projectName) {
		Query<Long> query = databaseManager.createQuery(getCountQuery(), Long.class);
		query.setParameter("projectName", projectName);
		return query.getSingleResult() > 0;
	}

	@Override
	public boolean hasResultsBefore(Date date, String projectName) {
		Query<Long> query = databaseManager.createQuery(getCountQuery("result.date < :date"), Long.class);
		query.setParameter("date", date.getTime());
		query.setParameter("projectName", projectName);
		return query.getSingleResult() > 0;
	}

	@Override
	public boolean hasResultsAfter(Date date, String projectName) {
		Query<Long> query = databaseManager.createQuery(getCountQuery("result.date > :date"), Long.class);
		query.setParameter("date", date.getTime());
		query.setParameter("projectName", projectName);
		return query.getSingleResult() > 0;
	}

	private String getCountQuery() {
		return getCountQuery("1 = 1");
	}

	private String getCountQuery(String condition) {
		return "SELECT count(result) FROM ProjectResult result " +
				"WHERE result.project.name = :projectName AND " + condition;
	}

	@Override
	public ProjectResult getFirstResultOf(String projectName) {
		Query<ProjectResultRecord> query = createRecordQuery(getFirstQuery());
		query.setParameter("projectName", projectName);
		return getResult(query);
	}

	@Override
	public ProjectResult getLastResultOf(String projectName) {
		Query<ProjectResultRecord> query = createRecordQuery(getLastQuery());
		query.setParameter("projectName", projectName);
		return getResult(query);
	}

	@Override
	public ProjectResult getFirstResultAfter(Date date, String projectName) {
		Query<ProjectResultRecord> query = createRecordQuery(getFirstQuery("result.date > :date"));
		query.setParameter("date", date.getTime());
		query.setParameter("projectName", projectName);
		return getResult(query);
	}

	@Override
	public ProjectResult getLastResultBefore(Date date, String projectName) {
		Query<ProjectResultRecord> query = createRecordQuery(getLastQuery("result.date < :date"));
		query.setParameter("date", date.getTime());
		query.setParameter("projectName", projectName);
		return getResult(query);
	}

	private String getFirstQuery() {
		return getFirstQuery("1 = 1");
	}

	private String getFirstQuery(String loadDateCondition) {
		return getLastQuery(loadDateCondition).replace("SELECT max", "SELECT min");
	}

	private String getLastQuery() {
		return getLastQuery("1 = 1");
	}

	private String getLastQuery(String loadDateCondition) {
		return "SELECT r FROM ProjectResult r WHERE r.project.name = :projectName AND r.date = " +
					"(SELECT max(result.date) FROM ProjectResult result WHERE result.project.name = :projectName " +
						"AND " + loadDateCondition + ")";
	}

	private ProjectResult getResult(Query<ProjectResultRecord> query) {
		return query.getSingleResult("No project result found").convert();
	}
}