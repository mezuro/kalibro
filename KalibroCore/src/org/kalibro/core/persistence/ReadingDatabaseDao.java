package org.kalibro.core.persistence;

import java.util.List;

import javax.persistence.TypedQuery;

import org.kalibro.Reading;
import org.kalibro.core.persistence.record.ReadingRecord;
import org.kalibro.dao.ReadingDao;
import org.kalibro.dto.ReadingDto;

/**
 * Database access implementation for {@link ReadingDao}.
 * 
 * @author Carlos Morais
 */
public class ReadingDatabaseDao extends DatabaseDao<Reading, ReadingRecord> implements ReadingDao {

	protected ReadingDatabaseDao(RecordManager recordManager) {
		super(recordManager, ReadingRecord.class);
	}

	@Override
	public List<Reading> readingsOf(Long groupId) {
		String queryString = "SELECT reading FROM Reading reading WHERE reading.group.id = :groupId";
		TypedQuery<ReadingRecord> query = createRecordQuery(queryString);
		query.setParameter("groupId", groupId);
		return ReadingDto.convert(query.getResultList());
	}

	@Override
	public Long save(Reading reading) {
		ReadingRecord record = new ReadingRecord(reading, reading.getGroupId());
		return recordManager.save(record).id();
	}

	@Override
	public void delete(Long readingId) {
		deleteById(readingId);
	}
}