package org.kalibro.service;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.kalibro.dao.ReadingDao;
import org.kalibro.service.xml.ReadingXml;

/**
 * End point to make {@link ReadingDao} interface available as Web service.
 * 
 * @author Carlos Morais
 */
@WebService(name = "ReadingEndpoint", serviceName = "ReadingEndpointService")
public interface ReadingEndpoint {

	@WebMethod
	@WebResult(name = "reading")
	List<ReadingXml> readingsOf(@WebParam(name = "groupId") Long groupId);

	@WebMethod
	@WebResult(name = "readingId")
	Long saveReading(@WebParam(name = "reading") ReadingXml reading);

	@WebMethod
	void deleteReading(@WebParam(name = "readingId") Long readingId);
}