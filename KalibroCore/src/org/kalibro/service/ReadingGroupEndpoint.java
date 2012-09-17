package org.kalibro.service;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.kalibro.dao.ReadingGroupDao;
import org.kalibro.service.xml.ReadingGroupXmlRequest;
import org.kalibro.service.xml.ReadingGroupXmlResponse;

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
	ReadingGroupXmlResponse getReadingGroup(@WebParam(name = "groupId") Long groupId);

	@WebMethod
	@WebResult(name = "readingGroup")
	List<ReadingGroupXmlResponse> allReadingGroups();

	@WebMethod
	@WebResult(name = "readingGroupId")
	Long saveReadingGroup(@WebParam(name = "readingGroup") ReadingGroupXmlRequest group);

	@WebMethod
	void deleteReadingGroup(@WebParam(name = "groupId") Long groupId);
}