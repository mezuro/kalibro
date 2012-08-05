package org.kalibro.core.persistence.database;

import java.util.List;

import org.analizo.AnalizoMetricCollector;
import org.checkstyle.CheckstyleMetricCollector;
import org.kalibro.core.MetricCollector;
import org.kalibro.core.model.BaseTool;
import org.kalibro.core.persistence.dao.BaseToolDao;
import org.kalibro.core.persistence.database.entities.BaseToolRecord;

class BaseToolDatabaseDao extends DatabaseDao<BaseTool, BaseToolRecord> implements BaseToolDao {

	protected BaseToolDatabaseDao(DatabaseManager databaseManager) {
		super(databaseManager, BaseToolRecord.class);
	}

	public void saveBaseTools() {
		save(AnalizoMetricCollector.class);
		save(CheckstyleMetricCollector.class);
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