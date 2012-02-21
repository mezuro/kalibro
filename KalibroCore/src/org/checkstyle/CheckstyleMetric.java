package org.checkstyle;

import static org.kalibro.core.model.enums.Granularity.*;

import com.puppycrawl.tools.checkstyle.DefaultConfiguration;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import org.kalibro.core.model.NativeMetric;
import org.kalibro.core.model.enums.Granularity;
import org.kalibro.core.model.enums.Language;
import org.kalibro.core.util.Identifier;

public enum CheckstyleMetric {

	FILE_LENGTH(CLASS, "^File length is (\\d+) lines \\(max allowed is -1\\)\\.$", "FileLength", "max", "-1"),
	NUMBER_OF_METHODS(CLASS, "^Total number of methods is (\\d+) \\(max allowed is -1\\)\\.$",
		"MethodCount", "maxTotal", "-1") {

		@Override
		public DefaultConfiguration addToConfiguration(DefaultConfiguration parent) {
			DefaultConfiguration treeWalker = new DefaultConfiguration("TreeWalker");
			super.addToConfiguration(treeWalker);
			parent.addChild(treeWalker);
			return treeWalker;
		}
	};

	public static Set<NativeMetric> supportedMetrics() {
		Set<NativeMetric> supportedMetrics = new HashSet<NativeMetric>();
		for (CheckstyleMetric metric : CheckstyleMetric.values())
			supportedMetrics.add(metric.getNativeMetric());
		return supportedMetrics;
	}

	private DefaultConfiguration configuration;
	private NativeMetric nativeMetric;
	private Pattern pattern;

	private CheckstyleMetric(Granularity scope, String regularExpression, String moduleName, String... properties) {
		initializeConfiguration(moduleName, properties);
		nativeMetric = new NativeMetric(toString(), scope, Language.JAVA);
		pattern = Pattern.compile(regularExpression);
	}

	private void initializeConfiguration(String moduleName, String... properties) {
		configuration = new DefaultConfiguration(moduleName);
		for (int i = 0; i < properties.length; i += 2)
			configuration.addAttribute(properties[i], properties[i + 1]);
	}

	@Override
	public String toString() {
		return Identifier.fromConstant(name()).asText();
	}

	public DefaultConfiguration addToConfiguration(DefaultConfiguration parent) {
		parent.addChild(configuration);
		return parent;
	}

	public NativeMetric getNativeMetric() {
		return nativeMetric;
	}

	public Pattern getPattern() {
		return pattern;
	}
}