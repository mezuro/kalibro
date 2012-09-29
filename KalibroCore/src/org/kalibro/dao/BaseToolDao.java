package org.kalibro.dao;

import java.util.List;

import org.kalibro.BaseTool;

public interface BaseToolDao {

	List<String> getBaseToolNames();

	BaseTool getBaseTool(String baseToolName);
}