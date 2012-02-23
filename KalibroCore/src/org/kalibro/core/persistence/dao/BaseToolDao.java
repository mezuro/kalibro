package org.kalibro.core.persistence.dao;

import java.util.List;

import org.kalibro.core.model.BaseTool;

public interface BaseToolDao {

	void save(BaseTool baseTool);

	List<String> getBaseToolNames();

	BaseTool getBaseTool(String baseToolName);
}