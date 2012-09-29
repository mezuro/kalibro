package org.kalibro.core.persistence;

import org.analizo.AnalizoMetricCollector;
import org.checkstyle.CheckstyleMetricCollector;
import org.cvsanaly.CVSAnalyMetricCollector;
import org.kalibro.BaseTool;
import org.kalibro.MetricCollector;
import org.kalibro.core.persistence.record.BaseToolRecord;
import org.kalibro.dao.BaseToolDao;

class BaseToolDatabaseDao extends DatabaseDao<BaseTool, BaseToolRecord> implements BaseToolDao {

	protected BaseToolDatabaseDao(RecordManager recordManager) {
		super(recordManager, BaseToolRecord.class);
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
		MetricCollector collector = collectorClass.newInstance();
		BaseTool baseTool = new BaseTool(collector.name(), collector.description());
		baseTool.setSupportedMetrics(collector.supportedMetrics());
		baseTool.setCollectorClass(collectorClass);
		if (!all().contains(baseTool.getName()))
			save(new BaseToolRecord(baseTool));
	}
}