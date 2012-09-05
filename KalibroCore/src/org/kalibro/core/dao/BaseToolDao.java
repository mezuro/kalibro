package org.kalibro.core.dao;

import java.util.List;

import org.kalibro.core.model.BaseTool;

public interface BaseToolDao {

	List<String> getBaseToolNames();

	BaseTool getBaseTool(String baseToolName);
}