package org.kalibro.service;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.kalibro.service.xml.ReadingXml;

@WebService
public interface ReadingEndpoint {

	@WebMethod
	@WebResult(name = "reading")
	List<ReadingXml> readingsOf(@WebParam(name = "groupId") Long groupId);

	@WebMethod
	void save(@WebParam(name = "reading") ReadingXml reading);

	@WebMethod
	void delete(@WebParam(name = "readingId") Long readingId);
}