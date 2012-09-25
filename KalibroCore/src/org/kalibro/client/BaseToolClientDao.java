package org.kalibro.client;

import java.util.List;

import org.kalibro.BaseTool;
import org.kalibro.dao.BaseToolDao;
import org.kalibro.service.BaseToolEndpoint;

class BaseToolClientDao extends EndpointClient<BaseToolEndpoint> implements BaseToolDao {

	protected BaseToolClientDao(String serviceAddress) {
		super(serviceAddress, BaseToolEndpoint.class);
	}

	@Override
	public List<String> getBaseToolNames() {
		return port.getBaseToolNames();
	}

	@Override
	public BaseTool getBaseTool(String baseToolName) {
		return port.getBaseTool(baseToolName).convert();
	}
}