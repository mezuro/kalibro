package org.kalibro.client;

import java.util.Date;
import java.util.SortedMap;
import java.util.SortedSet;

import org.kalibro.ModuleResult;
import org.kalibro.dao.ModuleResultDao;
import org.kalibro.dto.DataTransferObject;
import org.kalibro.service.ModuleResultEndpoint;

/**
 * {@link ModuleResultEndpoint} client implementation of {@link ModuleResultDao}.
 * 
 * @author Carlos Morais
 */
class ModuleResultClientDao extends EndpointClient<ModuleResultEndpoint> implements ModuleResultDao {

	ModuleResultClientDao(String serviceAddress) {
		super(serviceAddress, ModuleResultEndpoint.class);
	}

	@Override
	public ModuleResult get(Long moduleResultId) {
		return port.getModuleResult(moduleResultId).convert();
	}

	@Override
	public SortedSet<ModuleResult> childrenOf(Long moduleResultId) {
		return DataTransferObject.toSortedSet(port.childrenOf(moduleResultId));
	}

	@Override
	public SortedMap<Date, ModuleResult> historyOf(Long id) {
		// TODO Auto-generated method stub
		return null;
	}
}