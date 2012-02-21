package org.kalibro.service;

import java.util.Date;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.kalibro.service.entities.ProjectResultXml;

@WebService
public interface ProjectResultEndpoint {

	@WebMethod
	boolean hasResultsFor(String projectName);

	@WebMethod
	boolean hasResultsBefore(Date date, String projectName);

	@WebMethod
	boolean hasResultsAfter(Date date, String projectName);

	@WebMethod
	ProjectResultXml getFirstResultOf(String projectName);

	@WebMethod
	ProjectResultXml getLastResultOf(String projectName);

	@WebMethod
	ProjectResultXml getLastResultBefore(Date date, String projectName);

	@WebMethod
	ProjectResultXml getFirstResultAfter(Date date, String projectName);
}