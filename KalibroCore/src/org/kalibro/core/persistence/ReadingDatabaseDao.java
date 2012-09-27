package org.kalibro.core.persistence;

import java.util.List;

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
	public List<Reading> readingsOf(Long groupId) {
		TypedQuery<ReadingRecord> query = createRecordQuery("WHERE reading.group.id = :groupId ORDER BY reading.grade");
		query.setParameter("groupId", groupId);
		return DataTransferObject.toList(query.getResultList());
	}

	@Override
	public Long save(Reading reading) {
		ReadingRecord record = new ReadingRecord(reading, reading.getGroupId());
		return save(record).id();
	}

	@Override
	public void delete(Long readingId) {
		deleteById(readingId);
	}
}