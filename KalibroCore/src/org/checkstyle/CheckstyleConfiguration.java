package org.checkstyle;

import com.google.common.collect.ImmutableMap;
import com.puppycrawl.tools.checkstyle.DefaultConfiguration;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;
import com.puppycrawl.tools.checkstyle.api.Configuration;

public class CheckstyleConfiguration implements Configuration {

	private DefaultConfiguration configuration;

	public CheckstyleConfiguration() {
		configuration = new DefaultConfiguration("Checker");
		configuration.addAttribute("localeCountry", "");
		configuration.addAttribute("localeLanguage", "");
		addMetrics();
	}

	private void addMetrics() {
		DefaultConfiguration parent = configuration;
		for (CheckstyleMetric metric : CheckstyleMetric.values())
			parent = metric.addToConfiguration(parent);
	}

	@Override
	public String getName() {
		return configuration.getName();
	}

	@Override
	public String[] getAttributeNames() {
		return configuration.getAttributeNames();
	}

	@Override
	public String getAttribute(String name) throws CheckstyleException {
		return configuration.getAttribute(name);
	}

	@Override
	public Configuration[] getChildren() {
		return configuration.getChildren();
	}

	@Override
	public ImmutableMap<String, String> getMessages() {
		return configuration.getMessages();
	}
}