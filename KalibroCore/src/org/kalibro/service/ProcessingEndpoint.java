package org.kalibro.service;

import java.util.Date;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.kalibro.ProcessState;
import org.kalibro.dao.ProcessingDao;
import org.kalibro.service.xml.ProcessingXml;

/**
 * End point to make {@link ProcessingDao} interface available as Web service.
 * 
 * @author Carlos Morais
 */
@WebService(name = "ProcessingEndpoint", serviceName = "ProcessingEndpointService")
public interface ProcessingEndpoint {

	@WebMethod
	@WebResult(name = "exists")
	boolean hasProcessing(@WebParam(name = "repositoryId") Long repositoryId);

	@WebMethod
	@WebResult(name = "exists")
	boolean hasReadyProcessing(@WebParam(name = "repositoryId") Long repositoryId);

	@WebMethod
	@WebResult(name = "exists")
	boolean hasProcessingBefore(@WebParam(name = "date") Date date, @WebParam(name = "repositoryId") Long repositoryId);

	@WebMethod
	@WebResult(name = "exists")
	boolean hasProcessingAfter(@WebParam(name = "date") Date date, @WebParam(name = "repositoryId") Long repositoryId);

	@WebMethod
	@WebResult(name = "processState")
	ProcessState lastProcessingState(@WebParam(name = "repositoryId") Long repositoryId);

	@WebMethod
	@WebResult(name = "processing")
	ProcessingXml lastReadyProcessing(@WebParam(name = "repositoryId") Long repositoryId);

	@WebMethod
	@WebResult(name = "processing")
	ProcessingXml firstProcessing(@WebParam(name = "repositoryId") Long repositoryId);

	@WebMethod
	@WebResult(name = "processing")
	ProcessingXml lastProcessing(@WebParam(name = "repositoryId") Long repositoryId);

	@WebMethod
	@WebResult(name = "processing")
	ProcessingXml firstProcessingAfter(
		@WebParam(name = "date") Date date,
		@WebParam(name = "repositoryId") Long repositoryId);

	@WebMethod
	@WebResult(name = "processing")
	ProcessingXml lastProcessingBefore(
		@WebParam(name = "date") Date date,
		@WebParam(name = "repositoryId") Long repositoryId);
}