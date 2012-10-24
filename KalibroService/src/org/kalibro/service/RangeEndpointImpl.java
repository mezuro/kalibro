package org.kalibro.service;

import java.util.List;

import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.kalibro.dao.DaoFactory;
import org.kalibro.dao.RangeDao;
import org.kalibro.dto.DataTransferObject;
import org.kalibro.service.xml.RangeXmlRequest;
import org.kalibro.service.xml.RangeXmlResponse;

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
	public List<RangeXmlResponse> rangesOf(@WebParam(name = "metricConfigurationId") Long metricConfigurationId) {
		return DataTransferObject.createDtos(dao.rangesOf(metricConfigurationId), RangeXmlResponse.class);
	}

	@Override
	@WebResult(name = "rangeId")
	public Long saveRange(
		@WebParam(name = "range") RangeXmlRequest range,
		@WebParam(name = "metricConfigurationId") Long metricConfigurationId) {
		return dao.save(range.convert(), metricConfigurationId);
	}

	@Override
	public void deleteRange(@WebParam(name = "rangeId") Long rangeId) {
		dao.delete(rangeId);
	}
}