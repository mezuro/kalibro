package org.kalibro.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kalibro.core.model.BaseTool;
import org.kalibro.dao.BaseToolDao;

public class BaseToolDaoFake implements BaseToolDao {

	private Map<String, BaseTool> baseTools = new HashMap<String, BaseTool>();

	public void save(BaseTool baseTool) {
		baseTools.put(baseTool.getName(), baseTool);
	}

	@Override
	public List<String> getBaseToolNames() {
		return new ArrayList<String>(baseTools.keySet());
	}

	@Override
	public BaseTool getBaseTool(String baseToolName) {
		return baseTools.get(baseToolName);
	}
}