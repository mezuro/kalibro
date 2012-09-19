package org.kalibro.client;

import java.util.List;

import org.kalibro.Reading;
import org.kalibro.dao.ReadingDao;
import org.kalibro.dto.DataTransferObject;
import org.kalibro.service.ReadingEndpoint;
import org.kalibro.service.xml.ReadingXml;

/**
 * {@link ReadingEndpoint} client implementation of {@link ReadingDao}.
 * 
 * @author Carlos Morais
 */
class ReadingClientDao extends EndpointClient<ReadingEndpoint> implements ReadingDao {

	public ReadingClientDao(String serviceAddress) {
		super(serviceAddress, ReadingEndpoint.class);
	}

	@Override
	public List<Reading> readingsOf(Long groupId) {
		return DataTransferObject.convert(port.readingsOf(groupId));
	}

	@Override
	public Long save(Reading reading) {
		return port.saveReading(new ReadingXml(reading));
	}

	@Override
	public void delete(Long readingId) {
		port.deleteReading(readingId);
	}
}