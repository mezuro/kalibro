package org.kalibro.client;

import java.util.SortedSet;

import org.kalibro.ProcessingObserver;
import org.kalibro.dao.ProcessingObserverDao;
import org.kalibro.dto.DataTransferObject;
import org.kalibro.service.ProcessingObserverEndpoint;
import org.kalibro.service.xml.ProcessingObserverXml;

public class ProcessingObserverClientDao extends EndpointClient<ProcessingObserverEndpoint>
	implements ProcessingObserverDao {

	public ProcessingObserverClientDao(String serviceAddress) {
		super(serviceAddress, ProcessingObserverEndpoint.class);
	}

	@Override
	public SortedSet<ProcessingObserver> all() {
		return DataTransferObject.toSortedSet(port.allProcessingObservers());
	}

	@Override
	public Long save(ProcessingObserver processingObserver, Long repositoryId) {
		return port.saveProcessingObserver(new ProcessingObserverXml(processingObserver), repositoryId);
	}

	@Override
	public void delete(Long processingObserverId) {
		port.deleteProcessingObserver(processingObserverId);
	}
}