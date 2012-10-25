package org.kalibro.service;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.kalibro.dao.ReadingGroupDao;
import org.kalibro.service.xml.ReadingGroupXml;

/**
 * End point to make {@link ReadingGroupDao} interface available as Web service.
 * 
 * @author Carlos Morais
 */
@WebService(name = "ReadingGroupEndpoint", serviceName = "ReadingGroupEndpointService")
public interface ReadingGroupEndpoint {

	@WebMethod
	@WebResult(name = "exists")
	boolean readingGroupExists(@WebParam(name = "groupId") Long groupId);

	@WebMethod
	@WebResult(name = "readingGroup")
	ReadingGroupXml getReadingGroup(@WebParam(name = "groupId") Long groupId);

	@WebMethod
	@WebResult(name = "readingGroup")
	ReadingGroupXml readingGroupOf(@WebParam(name = "metricConfigurationId") Long metricConfigurationId);

	@WebMethod
	@WebResult(name = "readingGroup")
	List<ReadingGroupXml> allReadingGroups();

	@WebMethod
	@WebResult(name = "readingGroupId")
	Long saveReadingGroup(@WebParam(name = "readingGroup") ReadingGroupXml group);

	@WebMethod
	void deleteReadingGroup(@WebParam(name = "groupId") Long groupId);
}