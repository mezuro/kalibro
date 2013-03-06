package org.kalibro.core.persistence;

import java.util.*;

import javax.persistence.TypedQuery;

import org.kalibro.MetricResult;
import org.kalibro.ModuleResult;
import org.kalibro.core.persistence.record.DescendantResultRecord;
import org.kalibro.core.persistence.record.MetricResultRecord;
import org.kalibro.core.persistence.record.ModuleResultRecord;
import org.kalibro.dao.MetricResultDao;
import org.kalibro.dto.DataTransferObject;

/**
 * Database access implementation for {@link MetricResultDao}.
 * 
 * @author Carlos Morais
 */
class MetricResultDatabaseDao extends DatabaseDao<MetricResult, MetricResultRecord> implements MetricResultDao {

	MetricResultDatabaseDao() {
		super(MetricResultRecord.class);
	}

	@Override
	public List<Double> descendantResultsOf(Long metricResultId) {
		String queryString = "SELECT dr FROM DescendantResult dr, MetricResult mr WHERE " +
			"dr.moduleResult = mr.moduleResult AND dr.configuration = mr.configuration AND mr.id = :metricResultId";
		TypedQuery<DescendantResultRecord> query = createQuery(queryString, DescendantResultRecord.class);
		query.setParameter("metricResultId", metricResultId);
		return DataTransferObject.toList(query.getResultList());
	}

	@Override
	public SortedSet<MetricResult> metricResultsOf(Long moduleResultId) {
		TypedQuery<MetricResultRecord> query = createRecordQuery("metricResult.moduleResult.id = :moduleResultId");
		query.setParameter("moduleResultId", moduleResultId);
		return DataTransferObject.toSortedSet(query.getResultList());
	}

	@Override
	public SortedMap<Date, MetricResult> historyOf(String metricName, Long moduleResultId) {
		ModuleResult moduleResult = new ModuleResultDatabaseDao().get(moduleResultId);
		String moduleName = ModuleResultRecord.persistedName(moduleResult.getModule().getName());
		String repositoryId = "(SELECT p.repository FROM ModuleResult mor, Processing p " +
			"WHERE p.id = mor.processing AND mor.id = :moduleResultId)";
		TypedQuery<Object[]> query = createQuery("SELECT processing.date, metricResult " +
			"FROM MetricResult metricResult JOIN metricResult.moduleResult moduleResult, Processing processing " +
			"WHERE processing.id = moduleResult.processing AND metricResult.configuration.metricName = :metricName " +
			"AND moduleResult.moduleName = :moduleName AND processing.repository = " + repositoryId, Object[].class);
		query.setParameter("metricName", metricName);
		query.setParameter("moduleName", moduleName);
		query.setParameter("moduleResultId", moduleResultId);
		List<Object[]> results = query.getResultList();

		SortedMap<Date, MetricResult> history = new TreeMap<Date, MetricResult>();
		for (Object[] result : results)
			history.put(new Date((Long) result[0]), ((MetricResultRecord) result[1]).convert());
		return history;
	}

	public void save(MetricResult metricResult, Long moduleResultId) {
		save(new MetricResultRecord(metricResult, moduleResultId));
	}
}