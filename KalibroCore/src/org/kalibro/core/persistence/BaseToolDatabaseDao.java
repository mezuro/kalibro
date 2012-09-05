package org.kalibro.core.persistence;

import java.util.List;

import org.analizo.AnalizoMetricCollector;
import org.checkstyle.CheckstyleMetricCollector;
import org.cvsanaly.CVSAnalyMetricCollector;
import org.kalibro.core.MetricCollector;
import org.kalibro.core.dao.BaseToolDao;
import org.kalibro.core.model.BaseTool;
import org.kalibro.core.persistence.entities.BaseToolRecord;

class BaseToolDatabaseDao extends DatabaseDao<BaseTool, BaseToolRecord> implements BaseToolDao {

	protected BaseToolDatabaseDao(DatabaseManager databaseManager) {
		super(databaseManager, BaseToolRecord.class);
	}

	public void saveBaseTools() {
		save(AnalizoMetricCollector.class);
		save(CheckstyleMetricCollector.class);
		save(CVSAnalyMetricCollector.class);
	}

	private void save(Class<? extends MetricCollector> collectorClass) {
		try {
			doSave(collectorClass);
		} catch (Exception exception) {
			return;
		}
	}

	private void doSave(Class<? extends MetricCollector> collectorClass) throws Exception {
		BaseTool baseTool = collectorClass.newInstance().getBaseTool();
		baseTool.setCollectorClass(collectorClass);
		if (!getBaseToolNames().contains(baseTool.getName()))
			databaseManager.save(new BaseToolRecord(baseTool));
	}

	@Override
	public List<String> getBaseToolNames() {
		return getAllNames();
	}

	@Override
	public BaseTool getBaseTool(String baseToolName) {
		return getByName(baseToolName);
	}
}