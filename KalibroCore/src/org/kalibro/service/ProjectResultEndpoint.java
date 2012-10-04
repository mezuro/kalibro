package org.kalibro.service;

import java.util.Date;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.kalibro.service.xml.ProjectResultXml;

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
	ProjectResultXml getFirstResultOf(@WebParam(name = "projectName") String projectName);

	@WebMethod
	@WebResult(name = "processing")
	ProjectResultXml getLastResultOf(@WebParam(name = "projectName") String projectName);

	@WebMethod
	@WebResult(name = "processing")
	ProjectResultXml getLastResultBefore(
		@WebParam(name = "date") Date date,
		@WebParam(name = "projectName") String projectName);

	@WebMethod
	@WebResult(name = "processing")
	ProjectResultXml getFirstResultAfter(
		@WebParam(name = "date") Date date,
		@WebParam(name = "projectName") String projectName);
}