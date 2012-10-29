package org.kalibro.service;

import java.util.List;

import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.kalibro.dao.DaoFactory;
import org.kalibro.dao.RangeDao;
import org.kalibro.dto.DataTransferObject;
import org.kalibro.service.xml.RangeXml;

/**
 * Implementation of {@link RangeEndpoint}.
 * 
 * @author Carlos Morais
 */
@WebService(name = "RangeEndpoint", serviceName = "RangeEndpointService")
public class RangeEndpointImpl implements RangeEndpoint {

	private RangeDao dao;

	public RangeEndpointImpl() {
		this(DaoFactory.getRangeDao());
	}

	public RangeEndpointImpl(RangeDao rangeDao) {
		dao = rangeDao;
	}

	@Override
	@WebResult(name = "range")
	public List<RangeXml> rangesOf(@WebParam(name = "metricConfigurationId") Long metricConfigurationId) {
		return DataTransferObject.createDtos(dao.rangesOf(metricConfigurationId), RangeXml.class);
	}

	@Override
	@WebResult(name = "rangeId")
	public Long saveRange(
		@WebParam(name = "range") RangeXml range,
		@WebParam(name = "metricConfigurationId") Long metricConfigurationId) {
		return dao.save(range.convert(), metricConfigurationId);
	}

	@Override
	public void deleteRange(@WebParam(name = "rangeId") Long rangeId) {
		dao.delete(rangeId);
	}
}