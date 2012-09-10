package org.kalibro.client;

import java.util.List;

import org.kalibro.Reading;
import org.kalibro.core.dao.ReadingDao;
import org.kalibro.core.dto.ReadingDto;
import org.kalibro.service.ReadingEndpoint;
import org.kalibro.service.xml.ReadingXml;

public class ReadingClientDao extends EndpointClient<ReadingEndpoint> implements ReadingDao {

	public ReadingClientDao(String serviceAddress) {
		super(serviceAddress, ReadingEndpoint.class);
	}

	@Override
	public List<Reading> readingsOf(Long groupId) {
		return ReadingDto.convert(port.readingsOf(groupId));
	}

	@Override
	public void save(Reading reading) {
		reading.setId(port.save(new ReadingXml(reading)));
	}

	@Override
	public void delete(Long readingId) {
		port.delete(readingId);
	}
}