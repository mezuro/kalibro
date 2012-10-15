package org.kalibro.core.persistence;

import java.util.Date;

import javax.persistence.TypedQuery;

import org.kalibro.Processing;
import org.kalibro.core.persistence.record.ProcessingRecord;
import org.kalibro.dao.ProcessingDao;

public class ProcessingDatabaseDao extends DatabaseDao<Processing, ProcessingRecord> implements ProcessingDao {

	public ProcessingDatabaseDao(RecordManager recordManager) {
		super(recordManager, ProcessingRecord.class);
	}

	@Override
	public void save(Processing repositoryResult) {
		save(new ProcessingRecord(repositoryResult));
	}

	@Override
	public boolean hasResultsFor(String projectName) {
		TypedQuery<Long> query = recordManager().createQuery(getCountQuery(), Long.class);
		query.setParameter("projectName", projectName);
		return query.getSingleResult() > 0;
	}

	@Override
	public boolean hasResultsBefore(Date date, String projectName) {
		TypedQuery<Long> query = recordManager().createQuery(getCountQuery("result.date < :date"), Long.class);
		query.setParameter("date", date.getTime());
		query.setParameter("projectName", projectName);
		return query.getSingleResult() > 0;
	}

	@Override
	public boolean hasResultsAfter(Date date, String projectName) {
		TypedQuery<Long> query = recordManager().createQuery(getCountQuery("result.date > :date"), Long.class);
		query.setParameter("date", date.getTime());
		query.setParameter("projectName", projectName);
		return query.getSingleResult() > 0;
	}

	private String getCountQuery() {
		return getCountQuery("1 = 1");
	}

	private String getCountQuery(String condition) {
		return "SELECT count(result) FROM Processing result " +
			"WHERE result.project.name = :projectName AND " + condition;
	}

	@Override
	public Processing getFirstResultOf(String projectName) {
		TypedQuery<ProcessingRecord> query = createRecordQuery(getFirstQuery());
		query.setParameter("projectName", projectName);
		return getResult(query);
	}

	@Override
	public Processing getLastResultOf(String projectName) {
		TypedQuery<ProcessingRecord> query = createRecordQuery(getLastQuery());
		query.setParameter("projectName", projectName);
		return getResult(query);
	}

	@Override
	public Processing getFirstResultAfter(Date date, String projectName) {
		TypedQuery<ProcessingRecord> query = createRecordQuery(getFirstQuery("result.date > :date"));
		query.setParameter("date", date.getTime());
		query.setParameter("projectName", projectName);
		return getResult(query);
	}

	@Override
	public Processing getLastResultBefore(Date date, String projectName) {
		TypedQuery<ProcessingRecord> query = createRecordQuery(getLastQuery("result.date < :date"));
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
		return "SELECT r FROM Processing r WHERE r.project.name = :projectName AND r.date = " +
			"(SELECT max(result.date) FROM Processing result WHERE result.project.name = :projectName " +
			"AND " + loadDateCondition + ")";
	}

	private Processing getResult(TypedQuery<ProcessingRecord> query) {
		return query.getSingleResult().convert();
	}
}