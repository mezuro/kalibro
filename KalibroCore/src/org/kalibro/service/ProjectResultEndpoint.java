package org.kalibro.service;

import java.util.Date;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.kalibro.service.entities.ProjectResultXml;

@WebService
public interface ProjectResultEndpoint {

	@WebMethod
	public boolean hasResultsFor(String projectName);

	@WebMethod
	public boolean hasResultsBefore(Date date, String projectName);

	@WebMethod
	public boolean hasResultsAfter(Date date, String projectName);

	@WebMethod
	public ProjectResultXml getFirstResultOf(String projectName);

	@WebMethod
	public ProjectResultXml getLastResultOf(String projectName);

	@WebMethod
	public ProjectResultXml getLastResultBefore(Date date, String projectName);

	@WebMethod
	public ProjectResultXml getFirstResultAfter(Date date, String projectName);
}