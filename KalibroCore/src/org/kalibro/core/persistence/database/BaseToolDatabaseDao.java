package org.kalibro.core.persistence.database;

import java.util.List;

import org.kalibro.core.model.BaseTool;
import org.kalibro.core.persistence.dao.BaseToolDao;
import org.kalibro.core.persistence.database.entities.BaseToolRecord;

class BaseToolDatabaseDao extends DatabaseDao<BaseTool, BaseToolRecord> implements BaseToolDao {

	protected BaseToolDatabaseDao(DatabaseManager databaseManager) {
		super(databaseManager, BaseToolRecord.class);
	}

	@Override
	public void save(BaseTool baseTool) {
		databaseManager.save(new BaseToolRecord(baseTool));
	}

	@Override
	public List<String> getBaseToolNames() {
		return getAllNames();
	}

	@Override
	public BaseTool getBaseTool(String baseToolName) {
		return getByName(baseToolName);
	}
}