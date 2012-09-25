package org.kalibro;

import static org.kalibro.Granularity.*;
import static org.kalibro.ModuleFixtures.*;

public final class ModuleNodeFixtures {

	private static ModuleNode helloWorldRoot = newHelloWorldRoot();
	private static ModuleNode helloWorldLeaf = newHelloWorldLeaf();

	private static ModuleNode analizoCheckstyleTree = newAnalizoCheckstyleTree();
	private static ModuleNode analizoNode = newAnalizoNode();
	private static ModuleNode checkstyleNode = newCheckstyleNode();
	private static ModuleNode analizoMetricCollectorNode = newAnalizoMetricCollectorNode();
	private static ModuleNode analizoOutputParserNode = newAnalizoOutputParserNode();
	private static ModuleNode checkstyleMetricCollectorNode = newCheckstyleMetricCollectorNode();
	private static ModuleNode checkstyleOutputParserNode = newCheckstyleOutputParserNode();

	public static ModuleNode helloWorldRoot() {
		return helloWorldRoot;
	}

	public static ModuleNode newHelloWorldRoot() {
		ModuleNode root = new ModuleNode(newHelloWorldApplication());
		root.addChild(newHelloWorldLeaf());
		return root;
	}

	public static ModuleNode helloWorldLeaf() {
		return helloWorldLeaf;
	}

	public static ModuleNode newHelloWorldLeaf() {
		return new ModuleNode(newHelloWorldClass());
	}

	public static ModuleNode analizoCheckstyleTree() {
		return analizoCheckstyleTree;
	}

	public static ModuleNode newAnalizoCheckstyleTree() {
		ModuleNode org = newNode(PACKAGE, "org");
		org.addChild(newAnalizoNode());
		org.addChild(newCheckstyleNode());
		return org;
	}

	public static ModuleNode analizoNode() {
		return analizoNode;
	}

	public static ModuleNode newAnalizoNode() {
		ModuleNode analizo = newNode(PACKAGE, "org.analizo");
		analizo.addChild(newAnalizoMetricCollectorNode());
		analizo.addChild(newAnalizoOutputParserNode());
		return analizo;
	}

	public static ModuleNode checkstyleNode() {
		return checkstyleNode;
	}

	public static ModuleNode newCheckstyleNode() {
		ModuleNode checkstyle = newNode(PACKAGE, "org.checkstyle");
		checkstyle.addChild(newCheckstyleMetricCollectorNode());
		checkstyle.addChild(newCheckstyleOutputParserNode());
		return checkstyle;
	}

	public static ModuleNode analizoMetricCollectorNode() {
		return analizoMetricCollectorNode;
	}

	public static ModuleNode newAnalizoMetricCollectorNode() {
		return newNode(CLASS, "org.analizo.AnalizoMetricCollector");
	}

	public static ModuleNode analizoOutputParserNode() {
		return analizoOutputParserNode;
	}

	public static ModuleNode newAnalizoOutputParserNode() {
		return newNode(CLASS, "org.analizo.AnalizoOutputParser");
	}

	public static ModuleNode checkstyleMetricCollectorNode() {
		return checkstyleMetricCollectorNode;
	}

	public static ModuleNode newCheckstyleMetricCollectorNode() {
		return newNode(CLASS, "org.checkstyle.CheckstyleMetricCollector");
	}

	public static ModuleNode checkstyleOutputParserNode() {
		return checkstyleOutputParserNode;
	}

	public static ModuleNode newCheckstyleOutputParserNode() {
		return newNode(CLASS, "org.checkstyle.CheckstyleOutputParser");
	}

	public static ModuleNode newNode(Granularity moduleGranularity, String moduleName) {
		return new ModuleNode(new Module(moduleGranularity, moduleName));
	}

	private ModuleNodeFixtures() {
		return;
	}
}