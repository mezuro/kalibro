package org.kalibro.service.xml;

import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.kalibro.Configuration;
import org.kalibro.MetricConfiguration;
import org.kalibro.dto.ConfigurationDto;

/**
 * XML element for {@link Configuration} requests.
 * 
 * @author Carlos Morais
 */
@XmlRootElement(name = "configuration")
@XmlAccessorType(XmlAccessType.FIELD)
public class ConfigurationXmlRequest extends ConfigurationDto {

	@XmlElement
	private Long id;

	@XmlElement(required = true)
	private String name;

	@XmlElement
	private String description;

	@XmlElement(name = "metricConfiguration")
	private Collection<MetricConfigurationXmlRequest> metricConfigurations;

	public ConfigurationXmlRequest() {
		super();
	}

	public ConfigurationXmlRequest(Configuration configuration) {
		id = configuration.getId();
		name = configuration.getName();
		description = configuration.getDescription();
		metricConfigurations = createDtos(configuration.getMetricConfigurations(), MetricConfigurationXmlRequest.class);
	}

	@Override
	public Long id() {
		return id;
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public String description() {
		return description;
	}

	@Override
	public SortedSet<MetricConfiguration> metricConfigurations() {
		return metricConfigurations == null ? new TreeSet<MetricConfiguration>() : toSortedSet(metricConfigurations);
	}
}