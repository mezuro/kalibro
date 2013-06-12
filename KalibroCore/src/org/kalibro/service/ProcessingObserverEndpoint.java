package org.kalibro.service;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.kalibro.service.xml.ProcessingObserverXml;

@WebService(name = "ProcessingObserverEndpoint", serviceName = "ProcessingObserverEndpointService")
public interface ProcessingObserverEndpoint {

	@WebMethod
	@WebResult(name = "processingObserver")
	List<ProcessingObserverXml> allProcessingObservers();
	@WebMethod
	@WebResult(name = "processingObserverId")
	Long saveProcessingObserver(
		@WebParam(name = "processingObserver") ProcessingObserverXml processingObserver,
		@WebParam(name = "repositoryId") Long repositoryId);

	@WebMethod
	void deleteProcessingObserver(@WebParam(name = "processingObserverId") Long processingObserverId);
}
