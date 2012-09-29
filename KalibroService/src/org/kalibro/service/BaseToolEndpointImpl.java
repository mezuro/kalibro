package org.kalibro.service;

import java.util.List;

import javax.jws.WebResult;
import javax.jws.WebService;

import org.kalibro.dao.BaseToolDao;
import org.kalibro.dao.DaoFactory;
import org.kalibro.dto.DataTransferObject;
import org.kalibro.service.xml.BaseToolXml;

/**
 * Implementation of {@link BaseToolEndpoint}.
 * 
 * @author Carlos Morais
 */
@WebService(name = "BaseToolEndpoint", serviceName = "BaseToolEndpointService")
public class BaseToolEndpointImpl implements BaseToolEndpoint {

	private BaseToolDao dao;

	public BaseToolEndpointImpl() {
		this(DaoFactory.getBaseToolDao());
	}

	public BaseToolEndpointImpl(BaseToolDao baseToolDao) {
		dao = baseToolDao;
	}

	@Override
	@WebResult(name = "baseTool")
	public List<BaseToolXml> allBaseTools() {
		return DataTransferObject.createDtos(dao.all(), BaseToolXml.class);
	}
}