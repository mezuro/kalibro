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

	ReadingGroupDatabaseDao(RecordManager recordManager) {
		super(recordManager, ReadingGroupRecord.class);
	}

	@Override
	public ReadingGroup readingGroupOf(Long metricConfigurationId) {
		TypedQuery<ReadingGroupRecord> query = createRecordQuery("JOIN MetricConfiguration mConf WHERE mConf.id = :id");
		query.setParameter("id", metricConfigurationId);
		return query.getSingleResult().convert();
	}

	@Override
	public Long save(ReadingGroup group) {
		return save(new ReadingGroupRecord(group)).id();
	}
}