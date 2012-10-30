package org.kalibro.client;

import java.util.SortedSet;

import org.kalibro.ReadingGroup;
import org.kalibro.dao.ReadingGroupDao;
import org.kalibro.dto.DataTransferObject;
import org.kalibro.service.ReadingGroupEndpoint;
import org.kalibro.service.xml.ReadingGroupXml;

/**
 * {@link ReadingGroupEndpoint} client implementation of {@link ReadingGroupDao}.
 * 
 * @author Carlos Morais
 */
class ReadingGroupClientDao extends EndpointClient<ReadingGroupEndpoint> implements ReadingGroupDao {

	ReadingGroupClientDao(String serviceAddress) {
		super(serviceAddress, ReadingGroupEndpoint.class);
	}

	@Override
	public boolean exists(Long groupId) {
		return port.readingGroupExists(groupId);
	}

	@Override
	public ReadingGroup get(Long groupId) {
		return port.getReadingGroup(groupId).convert();
	}

	@Override
	public ReadingGroup readingGroupOf(Long metricConfigurationId) {
		return port.readingGroupOf(metricConfigurationId).convert();
	}

	@Override
	public SortedSet<ReadingGroup> all() {
		return DataTransferObject.toSortedSet(port.allReadingGroups());
	}

	@Override
	public Long save(ReadingGroup group) {
		return port.saveReadingGroup(new ReadingGroupXml(group));
	}

	@Override
	public void delete(Long groupId) {
		port.deleteReadingGroup(groupId);
	}
}