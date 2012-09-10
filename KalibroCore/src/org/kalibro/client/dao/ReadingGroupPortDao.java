package org.kalibro.client.dao;

import java.util.List;

import org.kalibro.ReadingGroup;
import org.kalibro.client.EndpointClient;
import org.kalibro.core.dao.ReadingGroupDao;
import org.kalibro.core.dto.ReadingGroupDto;
import org.kalibro.service.ReadingGroupEndpoint;
import org.kalibro.service.xml.ReadingGroupXmlRequest;

public class ReadingGroupPortDao extends EndpointClient<ReadingGroupEndpoint> implements ReadingGroupDao {

	public ReadingGroupPortDao(String serviceAddress) {
		super(serviceAddress, ReadingGroupEndpoint.class);
	}

	@Override
	public List<ReadingGroup> all() {
		return ReadingGroupDto.convert(port.all());
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