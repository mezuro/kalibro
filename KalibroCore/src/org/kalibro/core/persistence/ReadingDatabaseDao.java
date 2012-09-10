package org.kalibro.core.persistence;

import java.util.List;

import javax.persistence.TypedQuery;

import org.kalibro.Reading;
import org.kalibro.core.dao.ReadingDao;
import org.kalibro.core.dto.ReadingDto;
import org.kalibro.core.persistence.record.ReadingGroupRecord;
import org.kalibro.core.persistence.record.ReadingRecord;

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
	public void save(Reading reading) {
		ReadingRecord record = new ReadingRecord(reading, new ReadingGroupRecord(reading.getGroup()));
		Reading merged = recordManager.save(record).convert();
		reading.setId(merged.getId());
	}

	@Override
	public void delete(Long readingId) {
		deleteById(readingId);
	}
}