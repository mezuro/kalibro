package org.checkstyle;

import static org.kalibro.core.model.enums.Statistic.*;

import java.util.HashMap;
import java.util.Map;

import org.kalibro.core.Identifier;
import org.kalibro.core.model.NativeMetric;
import org.kalibro.core.model.enums.Granularity;
import org.kalibro.core.model.enums.Language;
import org.kalibro.core.model.enums.Statistic;

public enum CheckstyleMetric {

	AVERAGE_ANONYMOUS_CLASSES_LENGTH("AnonInnerLength", "max", "maxLen.anonInner"),
	AVERAGE_CYCLOMATIC_COMPLEXITY("CyclomaticComplexity", "max", "cyclomaticComplexity"),
	AVERAGE_FOR_DEPTH("NestedForDepth", "max", "nested.for.depth"),
	AVERAGE_IF_DEPTH("NestedIfDepth", "max", "nested.if.depth"),
	AVERAGE_TRY_DEPTH("NestedTryDepth", "max", "nested.try.depth"),
	AVERAGE_METHOD_LENGTH("MethodLength", "max", "maxLen.method"),
	AVERAGE_METHOD_NCSS("JavaNCSS", "methodMaximum", "ncss.method"),
	AVERAGE_NPATH_COMPLEXITY("NPathComplexity", "max", "npathComplexity"),
	AVERAGE_RETURN_COUNT("ReturnCount", "max", "return.count"),
	AVERAGE_THROWS_COUNT("ThrowsCount", "max", "throws.count"),
	DATA_ABSTRACTION_COUPLING("ClassDataAbstractionCoupling", "max", "classDataAbstractionCoupling", SUM),
	EXECUTABLE_STATEMENTS("ExecutableStatementCount", "max", "executableStatementCount", SUM),
	FAN_OUT("ClassFanOutComplexity", "max", "classFanOutComplexity", SUM),
	FILE_LENGTH("FileLength", "max", "maxLen.file", false),
	JAVA_NCSS("JavaNCSS", "fileMaximum", "ncss.file"),
	MAGIC_NUMBER_COUNT("MagicNumber", "", "magic.number", COUNT),
	NUMBER_OF_EMPTY_STATEMENTS("EmptyStatement", "", "empty.statement", COUNT),
	NUMBER_OF_INLINE_CONDITIONALS("AvoidInlineConditionals", "", "inline.conditional.avoid", COUNT),
	NUMBER_OF_METHODS("MethodCount", "maxTotal", "too.many.methods", SUM),
	NUMBER_OF_OUTER_TYPES("OuterTypeNumber", "max", "maxOuterTypes", SUM),
	NUMBER_OF_TODO_COMMENTS("TodoComment", "", "todo.match", COUNT),
	NUMBER_OF_TRAILING_COMMENTS("TrailingComment", "", "trailing.comments", COUNT),
	SIMPLIFIABLE_BOOLEAN_RETURNS("SimplifyBooleanReturn", "", "simplify.boolreturn", COUNT),
	SIMPLIFIABLE_BOOLEAN_EXPRESSIONS("SimplifyBooleanExpression", "", "simplify.expression", COUNT);

	private static Map<String, CheckstyleMetric> metrics;

	public static CheckstyleMetric getMetricFor(String messageKey) {
		return metrics.get(messageKey);
	}

	private static void addMetric(String messageKey, CheckstyleMetric metric) {
		if (metrics == null)
			metrics = new HashMap<String, CheckstyleMetric>();
		metrics.put(messageKey, metric);
	}

	private boolean treeWalker;
	private String moduleName;
	private String attributeName;
	private String messageKey;
	private Statistic aggregationType;

	private CheckstyleMetric(String moduleName, String attributeName, String messageKey) {
		this(moduleName, attributeName, messageKey, AVERAGE);
	}

	private CheckstyleMetric(String moduleName, String attributeName, String messageKey, boolean treeWalker) {
		this(moduleName, attributeName, messageKey, AVERAGE);
		this.treeWalker = treeWalker;
	}

	private CheckstyleMetric(String moduleName, String attributeName, String messageKey, Statistic aggregationType) {
		this.treeWalker = true;
		this.moduleName = moduleName;
		this.attributeName = attributeName;
		this.messageKey = messageKey;
		this.aggregationType = aggregationType;
		addMetric(messageKey, this);
	}

	@Override
	public String toString() {
		return Identifier.fromConstant(name()).asText();
	}

	protected String getMessageKey() {
		return messageKey;
	}

	protected NativeMetric getNativeMetric() {
		NativeMetric nativeMetric = new NativeMetric(toString(), Granularity.CLASS, Language.JAVA);
		nativeMetric.setOrigin("Checkstyle");
		return nativeMetric;
	}

	protected Statistic getAggregationType() {
		return aggregationType;
	}

	protected void addToChecker(CheckstyleConfiguration checker) {
		CheckstyleConfiguration configuration = getParent(checker).getChildByName(moduleName);
		configuration.addMessageKey(messageKey);
		if (!attributeName.isEmpty())
			configuration.addAttributeName(attributeName);
	}

	protected CheckstyleConfiguration getParent(CheckstyleConfiguration checker) {
		if (treeWalker)
			return checker.getChildByName("TreeWalker");
		return checker;
	}
}