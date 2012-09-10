package org.kalibro.service;

import java.util.ArrayList;
import java.util.List;

import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.kalibro.Reading;
import org.kalibro.core.dao.DaoFactory;
import org.kalibro.core.dao.ReadingDao;
import org.kalibro.service.xml.ReadingXml;

@WebService
public class ReadingEndpointImpl implements ReadingEndpoint {

	private ReadingDao dao;

	public ReadingEndpointImpl() {
		this(DaoFactory.getReadingDao());
	}

	protected ReadingEndpointImpl(ReadingDao readingDao) {
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
	public void save(@WebParam(name = "reading") ReadingXml reading) {
		dao.save(reading.convert());
	}

	@Override
	public void delete(@WebParam(name = "readingId") Long readingId) {
		dao.delete(readingId);
	}
}