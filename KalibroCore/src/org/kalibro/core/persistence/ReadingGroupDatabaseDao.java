package org.kalibro.core.persistence;

import java.util.List;

import org.kalibro.ReadingGroup;
import org.kalibro.core.dao.ReadingGroupDao;
import org.kalibro.core.persistence.record.ReadingGroupRecord;

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
	public void save(ReadingGroup group) {
		ReadingGroupRecord merged = recordManager.save(new ReadingGroupRecord(group));
		group.setId(merged.convert().getId());
	}

	@Override
	public void delete(Long groupId) {
		deleteById(groupId);
	}
}