package org.kalibro.service;

import java.util.List;

import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.kalibro.Kalibro;
import org.kalibro.core.model.BaseTool;
import org.kalibro.service.entities.BaseToolXml;

@WebService
public class BaseToolEndpointImpl implements BaseToolEndpoint {

	@Override
	@WebResult(name = "baseToolName")
	public List<String> getBaseToolNames() {
		return Kalibro.getBaseToolDao().getBaseToolNames();
	}

	@Override
	@WebResult(name = "baseTool")
	public BaseToolXml getBaseTool(@WebParam(name = "baseToolName") String baseToolName) {
		BaseTool baseTool = Kalibro.getBaseToolDao().getBaseTool(baseToolName);
		return new BaseToolXml(baseTool);
	}
}