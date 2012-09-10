package org.kalibro.client;

import java.util.List;

import org.kalibro.ReadingGroup;
import org.kalibro.dao.ReadingGroupDao;
import org.kalibro.dto.DataTransferObject;
import org.kalibro.service.ReadingGroupEndpoint;
import org.kalibro.service.xml.ReadingGroupXmlRequest;

/**
 * {@link ReadingGroupEndpoint} client implementation of {@link ReadingGroupDao}.
 * 
 * @author Carlos Morais
 */
class ReadingGroupClientDao extends EndpointClient<ReadingGroupEndpoint> implements ReadingGroupDao {

	public ReadingGroupClientDao(String serviceAddress) {
		super(serviceAddress, ReadingGroupEndpoint.class);
	}

	@Override
	public List<ReadingGroup> all() {
		return DataTransferObject.convert(port.all());
	}

	@Override
	public void save(ReadingGroup group) {
		group.setId(port.save(new ReadingGroupXmlRequest(group)));
	}

	@Override
	public void delete(Long groupId) {
		port.delete(groupId);
	}

	@Override
	public ReadingGroup get(Long groupId) {
		return port.get(groupId).convert();
	}
}