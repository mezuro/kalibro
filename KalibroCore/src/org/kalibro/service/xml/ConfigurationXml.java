package org.kalibro.service.xml;

import java.util.ArrayList;
import java.util.Collection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.kalibro.Configuration;
import org.kalibro.core.model.MetricConfiguration;
import org.kalibro.dto.DataTransferObject;

@XmlRootElement(name = "configuration")
@XmlAccessorType(XmlAccessType.FIELD)
public class ConfigurationXml extends DataTransferObject<Configuration> {

	private Long id;

	@XmlElement(required = true)
	private String name;

	private String description;

	@XmlElement(name = "metricConfiguration")
	private Collection<MetricConfigurationXml> metricConfigurations;

	public ConfigurationXml() {
		super();
	}

	public ConfigurationXml(Configuration configuration) {
		id = configuration.getId();
		name = configuration.getName();
		description = configuration.getDescription();
		initializeMetrics(configuration);
	}

	private void initializeMetrics(Configuration configuration) {
		metricConfigurations = new ArrayList<MetricConfigurationXml>();
		for (MetricConfiguration metricConfiguration : configuration.getMetricConfigurations())
			metricConfigurations.add(new MetricConfigurationXml(metricConfiguration));
	}

	@Override
	public Configuration convert() {
		Configuration configuration = new Configuration();
		configuration.setId(id);
		configuration.setName(name);
		configuration.setDescription(description);
		convertMetrics(configuration);
		return configuration;
	}

	private void convertMetrics(Configuration configuration) {
		if (metricConfigurations != null)
			for (MetricConfigurationXml metricConfiguration : metricConfigurations)
				configuration.addMetricConfiguration(metricConfiguration.convert());
	}
}