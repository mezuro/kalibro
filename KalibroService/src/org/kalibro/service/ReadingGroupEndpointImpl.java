package org.kalibro.service;

import java.util.ArrayList;
import java.util.List;

import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.kalibro.ReadingGroup;
import org.kalibro.dao.DaoFactory;
import org.kalibro.dao.ReadingGroupDao;
import org.kalibro.service.xml.ReadingGroupXmlRequest;
import org.kalibro.service.xml.ReadingGroupXmlResponse;

/**
 * Implementation of {@link ReadingGroupEndpoint}.
 * 
 * @author Carlos Morais
 */
@WebService
public class ReadingGroupEndpointImpl implements ReadingGroupEndpoint {

	private ReadingGroupDao dao;

	public ReadingGroupEndpointImpl() {
		this(DaoFactory.getReadingGroupDao());
	}

	protected ReadingGroupEndpointImpl(ReadingGroupDao readingGroupDao) {
		dao = readingGroupDao;
	}

	@Override
	@WebResult(name = "exists")
	public boolean exists(@WebParam(name = "groupId") Long groupId) {
		return dao.exists(groupId);
	}

	@Override
	@WebResult(name = "readingGroup")
	public ReadingGroupXmlResponse get(@WebParam(name = "groupId") Long groupId) {
		return new ReadingGroupXmlResponse(dao.get(groupId));
	}

	@Override
	@WebResult(name = "readingGroup")
	public List<ReadingGroupXmlResponse> all() {
		List<ReadingGroupXmlResponse> groups = new ArrayList<ReadingGroupXmlResponse>();
		for (ReadingGroup group : dao.all())
			groups.add(new ReadingGroupXmlResponse(group));
		return groups;
	}

	@Override
	@WebResult(name = "readingGroupId")
	public Long save(@WebParam(name = "readingGroup") ReadingGroupXmlRequest group) {
		return dao.save(group.convert());
	}

	@Override
	public void delete(@WebParam(name = "groupId") Long groupId) {
		dao.delete(groupId);
	}
}