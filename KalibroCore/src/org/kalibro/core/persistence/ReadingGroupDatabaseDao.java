package org.kalibro.core.persistence;

import java.util.List;

import org.kalibro.ReadingGroup;
import org.kalibro.core.persistence.record.ReadingGroupRecord;
import org.kalibro.dao.ReadingGroupDao;

/**
 * Database access implementation for {@link ReadingGroupDao}.
 * 
 * @author Carlos Morais
 */
public class ReadingGroupDatabaseDao extends DatabaseDao<ReadingGroup, ReadingGroupRecord> implements ReadingGroupDao {

	protected ReadingGroupDatabaseDao(RecordManager recordManager) {
		super(recordManager, ReadingGroupRecord.class);
	}

	@Override
	public List<ReadingGroup> all() {
		return allOrderedByName();
	}

	@Override
	public ReadingGroup get(Long groupId) {
		return getById(groupId);
	}

	@Override
	public void save(ReadingGroup group) {
		ReadingGroup merged = recordManager.save(new ReadingGroupRecord(group)).convert();
		group.setId(merged.getId());
		group.setReadings(merged.getReadings());
	}

	@Override
	public void delete(Long groupId) {
		deleteById(groupId);
	}
}