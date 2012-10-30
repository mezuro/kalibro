package org.kalibro.core.persistence;

import javax.persistence.TypedQuery;

import org.kalibro.ReadingGroup;
import org.kalibro.core.persistence.record.ReadingGroupRecord;
import org.kalibro.dao.ReadingGroupDao;

/**
 * Database access implementation for {@link ReadingGroupDao}.
 * 
 * @author Carlos Morais
 */
class ReadingGroupDatabaseDao extends DatabaseDao<ReadingGroup, ReadingGroupRecord> implements ReadingGroupDao {

	ReadingGroupDatabaseDao() {
		super(ReadingGroupRecord.class);
	}

	@Override
	public ReadingGroup readingGroupOf(Long metricConfigurationId) {
		String from = "MetricConfiguration metricConfiguration JOIN metricConfiguration.readingGroup readingGroup";
		TypedQuery<ReadingGroupRecord> query = createRecordQuery(from, "metricConfiguration.id = :id");
		query.setParameter("id", metricConfigurationId);
		return query.getSingleResult().convert();
	}

	@Override
	public Long save(ReadingGroup group) {
		return save(new ReadingGroupRecord(group)).id();
	}
}