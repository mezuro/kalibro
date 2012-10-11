package org.kalibro.service;

import java.util.Date;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.kalibro.service.xml.ProcessingXml;

@WebService(name = "ProjectResultEndpoint", serviceName = "ProjectResultEndpointService")
public interface ProjectResultEndpoint {

	@WebMethod
	@WebResult(name = "hasResults")
	boolean hasResultsFor(@WebParam(name = "projectName") String projectName);

	@WebMethod
	@WebResult(name = "hasResults")
	boolean hasResultsBefore(
		@WebParam(name = "date") Date date,
		@WebParam(name = "projectName") String projectName);

	@WebMethod
	@WebResult(name = "hasResults")
	boolean hasResultsAfter(
		@WebParam(name = "date") Date date,
		@WebParam(name = "projectName") String projectName);

	@WebMethod
	@WebResult(name = "processing")
	ProcessingXml getFirstResultOf(@WebParam(name = "projectName") String projectName);

	@WebMethod
	@WebResult(name = "processing")
	ProcessingXml getLastResultOf(@WebParam(name = "projectName") String projectName);

	@WebMethod
	@WebResult(name = "processing")
	ProcessingXml getLastResultBefore(
		@WebParam(name = "date") Date date,
		@WebParam(name = "projectName") String projectName);

	@WebMethod
	@WebResult(name = "processing")
	ProcessingXml getFirstResultAfter(
		@WebParam(name = "date") Date date,
		@WebParam(name = "projectName") String projectName);
}