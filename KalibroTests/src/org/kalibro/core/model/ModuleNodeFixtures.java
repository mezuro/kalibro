package org.kalibro.core.model;

import static org.kalibro.core.model.ModuleFixtures.*;
import static org.kalibro.core.model.enums.Granularity.*;

import org.kalibro.core.model.enums.Granularity;

public final class ModuleNodeFixtures {

	public static ModuleNode helloWorldTree() {
		ModuleNode root = new ModuleNode(helloWorldApplication());
		root.addChild(helloWorldNode());
		return root;
	}

	public static ModuleNode helloWorldNode() {
		return new ModuleNode(helloWorldClass());
	}

	public static ModuleNode junitAnalizoTree() {
		ModuleNode org = newNode(PACKAGE, "org");
		org.addChild(analizoNode());
		org.addChild(junitNode());
		return org;
	}

	public static ModuleNode analizoNode() {
		ModuleNode analizoNode = newNode(PACKAGE, "org.analizo");
		analizoNode.addChild(analizoMetricCollectorNode());
		analizoNode.addChild(analizoResultParserNode());
		return analizoNode;
	}

	public static ModuleNode analizoMetricCollectorNode() {
		return newNode(CLASS, "org.analizo.AnalizoMetricCollector");
	}

	public static ModuleNode analizoResultParserNode() {
		return newNode(CLASS, "org.analizo.AnalizoOutputParser");
	}

	public static ModuleNode junitNode() {
		ModuleNode junitNode = newNode(PACKAGE, "org.junit");
		junitNode.addChild(assertNode());
		junitNode.addChild(comparisonFailureNode());
		return junitNode;
	}

	public static ModuleNode assertNode() {
		return newNode(CLASS, "org.junit.Assert");
	}

	public static ModuleNode comparisonFailureNode() {
		return newNode(CLASS, "org.junit.ComparisonFailure");
	}

	public static ModuleNode newNode(Granularity moduleGranularity, String moduleName) {
		return new ModuleNode(new Module(moduleGranularity, moduleName));
	}

	private ModuleNodeFixtures() {
		return;
	}
}