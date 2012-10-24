package org.kalibro.service;

import java.util.List;

import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.kalibro.dao.DaoFactory;
import org.kalibro.dao.ModuleResultDao;
import org.kalibro.dto.DataTransferObject;
import org.kalibro.service.xml.ModuleResultXml;

/**
 * Implementation of {@link ModuleResultEndpoint}.
 * 
 * @author Carlos Morais
 */
@WebService(name = "ModuleResultEndpoint", serviceName = "ModuleResultEndpointService")
public class ModuleResultEndpointImpl implements ModuleResultEndpoint {

	private ModuleResultDao dao;

	public ModuleResultEndpointImpl() {
		this(DaoFactory.getModuleResultDao());
	}

	public ModuleResultEndpointImpl(ModuleResultDao moduleResultDao) {
		dao = moduleResultDao;
	}

	@Override
	@WebResult(name = "moduleResult")
	public ModuleResultXml resultsRootOf(@WebParam(name = "processingId") Long processingId) {
		return new ModuleResultXml(dao.resultsRootOf(processingId));
	}

	@Override
	@WebResult(name = "moduleResult")
	public ModuleResultXml parentOf(@WebParam(name = "moduleResultId") Long moduleResultId) {
		return new ModuleResultXml(dao.parentOf(moduleResultId));
	}

	@Override
	@WebResult(name = "moduleResult")
	public List<ModuleResultXml> childrenOf(@WebParam(name = "moduleResultId") Long moduleResultId) {
		return DataTransferObject.createDtos(dao.childrenOf(moduleResultId), ModuleResultXml.class);
	}
}