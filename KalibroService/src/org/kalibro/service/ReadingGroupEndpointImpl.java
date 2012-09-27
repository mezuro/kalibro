package org.kalibro.service;

import java.util.List;

import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.kalibro.dao.DaoFactory;
import org.kalibro.dao.ReadingGroupDao;
import org.kalibro.dto.DataTransferObject;
import org.kalibro.service.xml.ReadingGroupXmlRequest;
import org.kalibro.service.xml.ReadingGroupXmlResponse;

/**
 * Implementation of {@link ReadingGroupEndpoint}.
 * 
 * @author Carlos Morais
 */
@WebService(name = "ReadingGroupEndpoint", serviceName = "ReadingGroupEndpointService")
public class ReadingGroupEndpointImpl implements ReadingGroupEndpoint {

	private ReadingGroupDao dao;

	public ReadingGroupEndpointImpl() {
		this(DaoFactory.getReadingGroupDao());
	}

	public ReadingGroupEndpointImpl(ReadingGroupDao readingGroupDao) {
		dao = readingGroupDao;
	}

	@Override
	@WebResult(name = "exists")
	public boolean readingGroupExists(@WebParam(name = "groupId") Long groupId) {
		return dao.exists(groupId);
	}

	@Override
	@WebResult(name = "readingGroup")
	public ReadingGroupXmlResponse getReadingGroup(@WebParam(name = "groupId") Long groupId) {
		return new ReadingGroupXmlResponse(dao.get(groupId));
	}

	@Override
	@WebResult(name = "readingGroup")
	public List<ReadingGroupXmlResponse> allReadingGroups() {
		return DataTransferObject.createDtos(dao.all(), ReadingGroupXmlResponse.class);
	}

	@Override
	@WebResult(name = "readingGroupId")
	public Long saveReadingGroup(@WebParam(name = "readingGroup") ReadingGroupXmlRequest group) {
		return dao.save(group.convert());
	}

	@Override
	public void deleteReadingGroup(@WebParam(name = "groupId") Long groupId) {
		dao.delete(groupId);
	}
}