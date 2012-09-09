package org.kalibro.client.dao;

import java.util.List;

import org.kalibro.client.EndpointClient;
import org.kalibro.core.dao.BaseToolDao;
import org.kalibro.core.model.BaseTool;
import org.kalibro.service.BaseToolEndpoint;

class BaseToolPortDao extends EndpointClient<BaseToolEndpoint> implements BaseToolDao {

	protected BaseToolPortDao(String serviceAddress) {
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