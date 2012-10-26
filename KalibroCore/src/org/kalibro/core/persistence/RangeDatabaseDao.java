package org.kalibro.core.persistence;

import java.util.SortedSet;

import javax.persistence.TypedQuery;

import org.kalibro.Range;
import org.kalibro.core.persistence.record.RangeRecord;
import org.kalibro.dao.RangeDao;
import org.kalibro.dto.DataTransferObject;

/**
 * Database access implementation for {@link RangeDao}.
 * 
 * @author Carlos Morais
 */
class RangeDatabaseDao extends DatabaseDao<Range, RangeRecord> implements RangeDao {

	RangeDatabaseDao() {
		super(RangeRecord.class);
	}

	@Override
	public SortedSet<Range> rangesOf(Long metricConfigurationId) {
		TypedQuery<RangeRecord> query = createRecordQuery("range.configuration.id = :configurationId");
		query.setParameter("configurationId", metricConfigurationId);
		return DataTransferObject.toSortedSet(query.getResultList());
	}

	@Override
	public Long save(Range range, Long metricConfigurationId) {
		return save(new RangeRecord(range, metricConfigurationId)).id();
	}
}