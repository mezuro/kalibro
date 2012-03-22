package org.kalibro.service;

import java.util.Date;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.kalibro.service.entities.ProjectResultXml;

@WebService
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
	@WebResult(name = "projectResult")
	ProjectResultXml getFirstResultOf(@WebParam(name = "projectName") String projectName);

	@WebMethod
	@WebResult(name = "projectResult")
	ProjectResultXml getLastResultOf(@WebParam(name = "projectName") String projectName);

	@WebMethod
	@WebResult(name = "projectResult")
	ProjectResultXml getLastResultBefore(
		@WebParam(name = "date") Date date,
		@WebParam(name = "projectName") String projectName);

	@WebMethod
	@WebResult(name = "projectResult")
	ProjectResultXml getFirstResultAfter(
		@WebParam(name = "date") Date date,
		@WebParam(name = "projectName") String projectName);
}