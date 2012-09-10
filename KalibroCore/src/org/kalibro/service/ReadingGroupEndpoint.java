package org.kalibro.service;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.kalibro.service.xml.ReadingGroupXmlRequest;
import org.kalibro.service.xml.ReadingGroupXmlResponse;

@WebService
public interface ReadingGroupEndpoint {

	@WebMethod
	@WebResult(name = "readingGroup")
	List<ReadingGroupXmlResponse> all();

	@WebMethod
	@WebResult(name = "readingGroup")
	ReadingGroupXmlResponse get(@WebParam(name = "groupId") Long groupId);

	@WebMethod
	@WebResult(name = "readingGroupId")
	Long save(@WebParam(name = "readingGroup") ReadingGroupXmlRequest group);

	@WebMethod
	void delete(@WebParam(name = "groupId") Long groupId);
}