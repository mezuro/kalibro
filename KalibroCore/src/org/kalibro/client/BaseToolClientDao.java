package org.kalibro.client;

import java.util.SortedSet;

import org.kalibro.BaseTool;
import org.kalibro.dao.BaseToolDao;
import org.kalibro.dto.DataTransferObject;
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
	public SortedSet<BaseTool> all() {
		return DataTransferObject.toSortedSet(port.allBaseTools());
	}
}