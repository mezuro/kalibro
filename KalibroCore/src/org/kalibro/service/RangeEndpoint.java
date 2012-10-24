package org.kalibro.service;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.kalibro.dao.RangeDao;
import org.kalibro.service.xml.RangeXmlRequest;
import org.kalibro.service.xml.RangeXmlResponse;

/**
 * End point to make {@link RangeDao} interface available as Web service.
 * 
 * @author Carlos Morais
 */
@WebService(name = "RangeEndpoint", serviceName = "RangeEndpointService")
public interface RangeEndpoint {

	@WebMethod
	@WebResult(name = "range")
	List<RangeXmlResponse> rangesOf(@WebParam(name = "metricConfigurationId") Long metricConfigurationId);

	@WebMethod
	@WebResult(name = "rangeId")
	Long saveRange(
		@WebParam(name = "range") RangeXmlRequest range,
		@WebParam(name = "metricConfigurationId") Long metricConfigurationId);

	@WebMethod
	void deleteRange(@WebParam(name = "rangeId") Long rangeId);
}