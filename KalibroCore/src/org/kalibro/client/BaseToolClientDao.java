package org.kalibro.client;

import java.util.SortedSet;
import java.util.TreeSet;

import org.kalibro.BaseTool;
import org.kalibro.dao.BaseToolDao;
import org.kalibro.service.BaseToolEndpoint;

/**
 * {@link BaseToolEndpoint} client implementation of {@link BaseToolDao}.
 * 
 * @author Carlos Morais
 */
class BaseToolClientDao extends EndpointClient<BaseToolEndpoint> implements BaseToolDao {

	BaseToolClientDao(String serviceAddress) {
		super(serviceAddress, BaseToolEndpoint.class);
	}

	@Override
	public SortedSet<String> allNames() {
		return new TreeSet<String>(port.allBaseToolNames());
	}

	@Override
	public BaseTool get(String baseToolName) {
		return port.getBaseTool(baseToolName).convert();
	}
}