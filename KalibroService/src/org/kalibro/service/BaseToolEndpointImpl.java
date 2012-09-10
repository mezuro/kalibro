package org.kalibro.service;

import java.util.List;

import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.kalibro.dao.BaseToolDao;
import org.kalibro.dao.DaoFactory;
import org.kalibro.service.entities.BaseToolXml;

@WebService
public class BaseToolEndpointImpl implements BaseToolEndpoint {

	private BaseToolDao dao;

	public BaseToolEndpointImpl() {
		this(DaoFactory.getBaseToolDao());
	}

	protected BaseToolEndpointImpl(BaseToolDao baseToolDao) {
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
		return new BaseToolXml(dao.getBaseTool(baseToolName));
	}
}