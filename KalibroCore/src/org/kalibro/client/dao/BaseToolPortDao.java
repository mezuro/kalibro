package org.kalibro.client.dao;

import java.util.List;

import org.kalibro.client.EndpointPortFactory;
import org.kalibro.core.model.BaseTool;
import org.kalibro.core.persistence.dao.BaseToolDao;
import org.kalibro.service.BaseToolEndpoint;

class BaseToolPortDao implements BaseToolDao {

	private BaseToolEndpoint port;

	protected BaseToolPortDao() {
		port = EndpointPortFactory.getEndpointPort(BaseToolEndpoint.class);
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