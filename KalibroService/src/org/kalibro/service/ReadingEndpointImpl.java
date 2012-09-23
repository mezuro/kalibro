package org.kalibro.service;

import java.util.ArrayList;
import java.util.List;

import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.kalibro.Reading;
import org.kalibro.dao.DaoFactory;
import org.kalibro.dao.ReadingDao;
import org.kalibro.service.xml.ReadingXml;

/**
 * Implementation of {@link ReadingEndpoint}.
 * 
 * @author Carlos Morais
 */
@WebService(name = "ReadingEndpoint", serviceName = "ReadingEndpointService")
public class ReadingEndpointImpl implements ReadingEndpoint {

	private ReadingDao dao;

	public ReadingEndpointImpl() {
		this(DaoFactory.getReadingDao());
	}

	public ReadingEndpointImpl(ReadingDao readingDao) {
		dao = readingDao;
	}

	@Override
	@WebResult(name = "reading")
	public List<ReadingXml> readingsOf(@WebParam(name = "groupId") Long groupId) {
		List<ReadingXml> readings = new ArrayList<ReadingXml>();
		for (Reading reading : dao.readingsOf(groupId))
			readings.add(new ReadingXml(reading));
		return readings;
	}

	@Override
	@WebResult(name = "readingId")
	public Long saveReading(@WebParam(name = "reading") ReadingXml reading) {
		return dao.save(reading.convert());
	}

	@Override
	public void deleteReading(@WebParam(name = "readingId") Long readingId) {
		dao.delete(readingId);
	}
}