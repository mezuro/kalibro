package org.kalibro.client;

import java.util.SortedSet;

import org.kalibro.Range;
import org.kalibro.dao.RangeDao;
import org.kalibro.dto.DataTransferObject;
import org.kalibro.service.RangeEndpoint;
import org.kalibro.service.xml.RangeXml;

/**
 * {@link RangeEndpoint} client implementation of {@link RangeDao}.
 * 
 * @author Carlos Morais
 */
class RangeClientDao extends EndpointClient<RangeEndpoint> implements RangeDao {

	RangeClientDao(String serviceAddress) {
		super(serviceAddress, RangeEndpoint.class);
	}

	@Override
	public SortedSet<Range> rangesOf(Long metricConfigurationId) {
		return DataTransferObject.toSortedSet(port.rangesOf(metricConfigurationId));
	}

	@Override
	public Long save(Range range, Long metricConfigurationId) {
		return port.saveRange(new RangeXml(range), metricConfigurationId);
	}

	@Override
	public void delete(Long rangeId) {
		port.deleteRange(rangeId);
	}
}