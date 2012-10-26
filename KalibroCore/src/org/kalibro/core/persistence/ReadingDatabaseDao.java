package org.kalibro.core.persistence;

import java.util.SortedSet;

import javax.persistence.TypedQuery;

import org.kalibro.Reading;
import org.kalibro.core.persistence.record.ReadingRecord;
import org.kalibro.dao.ReadingDao;
import org.kalibro.dto.DataTransferObject;

/**
 * Database access implementation for {@link ReadingDao}.
 * 
 * @author Carlos Morais
 */
class ReadingDatabaseDao extends DatabaseDao<Reading, ReadingRecord> implements ReadingDao {

	ReadingDatabaseDao(RecordManager recordManager) {
		super(recordManager, ReadingRecord.class);
	}

	@Override
	public Reading readingOf(Long rangeId) {
		String from = "Range range JOIN range.reading reading";
		TypedQuery<ReadingRecord> query = createRecordQuery(from, "range.id = :rangeId");
		query.setParameter("rangeId", rangeId);
		return query.getSingleResult().convert();
	}

	@Override
	public SortedSet<Reading> readingsOf(Long groupId) {
		TypedQuery<ReadingRecord> query = createRecordQuery("reading.group.id = :groupId");
		query.setParameter("groupId", groupId);
		return DataTransferObject.toSortedSet(query.getResultList());
	}

	@Override
	public Long save(Reading reading, Long groupId) {
		return save(new ReadingRecord(reading, groupId)).id();
	}
}