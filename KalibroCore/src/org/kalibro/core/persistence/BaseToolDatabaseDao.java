package org.kalibro.core.persistence;

import javax.persistence.Query;

import org.kalibro.BaseTool;
import org.kalibro.core.persistence.record.BaseToolRecord;
import org.kalibro.dao.BaseToolDao;

class BaseToolDatabaseDao extends DatabaseDao<BaseTool, BaseToolRecord> implements BaseToolDao {

	protected BaseToolDatabaseDao(RecordManager recordManager) {
		super(recordManager, BaseToolRecord.class);
	}

	public void saveBaseTools() {
		save("org.analizo.AnalizoMetricCollector");
		save("org.checkstyle.CheckstyleMetricCollector");
		save("org.cvsanaly.CVSAnalyMetricCollector");
	}

	private void save(String collectorClassName) {
		if (notSaved(collectorClassName))
			save(new BaseToolRecord(new BaseTool(collectorClassName)));
	}

	private boolean notSaved(String collectorClassName) {
		Query query = createQuery("SELECT 1 FROM BaseTool WHERE collectorClass = :collectorClass");
		query.setParameter("collectorClass", collectorClassName);
		return query.getResultList().isEmpty();
	}
}