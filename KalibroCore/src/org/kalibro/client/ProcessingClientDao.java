package org.kalibro.client;

import java.util.Date;

import org.kalibro.ProcessState;
import org.kalibro.Processing;
import org.kalibro.dao.ProcessingDao;
import org.kalibro.service.ProcessingEndpoint;

/**
 * {@link ProcessingEndpoint} client implementation of {@link ProcessingDao}.
 * 
 * @author Carlos Morais
 */
class ProcessingClientDao extends EndpointClient<ProcessingEndpoint> implements ProcessingDao {

	ProcessingClientDao(String serviceAddress) {
		super(serviceAddress, ProcessingEndpoint.class);
	}

	@Override
	public boolean hasProcessing(Long repositoryId) {
		return port.hasProcessing(repositoryId);
	}

	@Override
	public boolean hasReadyProcessing(Long repositoryId) {
		return port.hasReadyProcessing(repositoryId);
	}

	@Override
	public boolean hasProcessingBefore(Date date, Long repositoryId) {
		return port.hasProcessingBefore(date, repositoryId);
	}

	@Override
	public boolean hasProcessingAfter(Date date, Long repositoryId) {
		return port.hasProcessingAfter(date, repositoryId);
	}

	@Override
	public ProcessState lastProcessingState(Long repositoryId) {
		return port.lastProcessingState(repositoryId);
	}

	@Override
	public Processing lastReadyProcessing(Long repositoryId) {
		return port.lastReadyProcessing(repositoryId).convert();
	}

	@Override
	public Processing firstProcessing(Long repositoryId) {
		return port.firstProcessing(repositoryId).convert();
	}

	@Override
	public Processing lastProcessing(Long repositoryId) {
		return port.lastProcessing(repositoryId).convert();
	}

	@Override
	public Processing firstProcessingAfter(Date date, Long repositoryId) {
		return port.firstProcessingAfter(date, repositoryId).convert();
	}

	@Override
	public Processing lastProcessingBefore(Date date, Long repositoryId) {
		return port.lastProcessingBefore(date, repositoryId).convert();
	}
}