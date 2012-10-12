package org.kalibro.client;

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
	public ModuleResult resultsRootOf(Long processingId) {
		return port.resultsRootOf(processingId).convert();
	}

	@Override
	public ModuleResult parentOf(Long moduleResultId) {
		return port.parentOf(moduleResultId).convert();
	}

	@Override
	public SortedSet<ModuleResult> childrenOf(Long moduleResultId) {
		return DataTransferObject.toSortedSet(port.childrenOf(moduleResultId));
	}
}