package org.kalibro.core.persistence.dao;

import java.util.List;

import org.kalibro.core.model.BaseTool;

public interface BaseToolDao {

	public void save(BaseTool baseTool);

	public List<String> getBaseToolNames();

	public BaseTool getBaseTool(String baseToolName);
}