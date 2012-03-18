package org.kalibro.service;

import java.util.List;

import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.kalibro.Kalibro;
import org.kalibro.core.model.BaseTool;
import org.kalibro.core.persistence.dao.BaseToolDao;
import org.kalibro.service.entities.BaseToolXml;

@WebService
public class BaseToolEndpointImpl implements BaseToolEndpoint {

	private BaseToolDao dao;

	public BaseToolEndpointImpl() {
		this(Kalibro.getBaseToolDao());
	}

	public BaseToolEndpointImpl(BaseToolDao baseToolDao) {
		dao = baseToolDao;
	}

	@Override
	@WebResult(name = "baseToolName")
	public List<String> getBaseToolNames() {
		return dao.getBaseToolNames();
	}

	@Override
	@WebResult(name = "baseTool")
	public BaseToolXml getBaseTool(@WebParam(name = "baseToolName") String baseToolName) {
		BaseTool baseTool = dao.getBaseTool(baseToolName);
		return new BaseToolXml(baseTool);
	}
}