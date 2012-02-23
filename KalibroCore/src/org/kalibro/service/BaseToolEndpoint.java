package org.kalibro.service;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.kalibro.service.entities.BaseToolXml;

@WebService
public interface BaseToolEndpoint {

	@WebMethod
	List<String> getBaseToolNames();

	@WebMethod
	BaseToolXml getBaseTool(String baseToolName);
}