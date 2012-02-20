package org.checkstyle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.puppycrawl.tools.checkstyle.api.Configuration;

public class CheckstyleConfiguration implements Configuration {

	public static CheckstyleConfiguration checkerConfiguration() {
		CheckstyleConfiguration checker = new CheckstyleConfiguration("Checker");
		for (CheckstyleMetric metric : CheckstyleMetric.values())
			metric.addToChecker(checker);
		return checker;
	}

	private String name;
	private List<String> attributes;
	private Map<String, String> messages;
	private Map<String, CheckstyleConfiguration> children;

	public CheckstyleConfiguration(String name) {
		this.name = name;
		attributes = new ArrayList<String>();
		messages = new HashMap<String, String>();
		children = new HashMap<String, CheckstyleConfiguration>();
	}

	@Override
	public String getName() {
		return name;
	}

	protected void addAttributeName(String attributeName) {
		attributes.add(attributeName);
	}

	@Override
	public String[] getAttributeNames() {
		return attributes.toArray(new String[attributes.size()]);
	}

	@Override
	public String getAttribute(String attributeName) {
		return "-1";
	}

	protected void addMessageKey(String messageKey) {
		messages.put(messageKey, "{0}");
	}

	@Override
	public ImmutableMap<String, String> getMessages() {
		return ImmutableMap.copyOf(messages);
	}

	protected CheckstyleConfiguration getChildByName(String childName) {
		if (! children.containsKey(childName))
			children.put(childName, new CheckstyleConfiguration(childName));
		return children.get(childName);
	}

	@Override
	public Configuration[] getChildren() {
		return children.values().toArray(new CheckstyleConfiguration[children.size()]);
	}
}