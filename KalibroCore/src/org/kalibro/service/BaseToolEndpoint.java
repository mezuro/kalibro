package org.kalibro.service;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.kalibro.service.entities.BaseToolXml;

@WebService
public interface BaseToolEndpoint {

	@WebMethod
	public List<String> getBaseToolNames();

	@WebMethod
	public BaseToolXml getBaseTool(String baseToolName);
}