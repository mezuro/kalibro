package org.checkstyle;

import static org.checkstyle.CheckstyleMetric.*;
import static org.junit.Assert.*;
import static org.kalibro.core.model.enums.Granularity.*;
import static org.kalibro.core.model.enums.Language.*;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.model.NativeMetric;

public class CheckstyleMetricTest extends KalibroTestCase {

	private CheckstyleConfiguration checker, treeWalker;

	@Before
	public void setUp() {
		checker = new CheckstyleConfiguration("Checker");
		for (CheckstyleMetric metric : CheckstyleMetric.values())
			metric.addToChecker(checker);
		treeWalker = checker.getChildByName("TreeWalker");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkSupportedMetrics() {
		assertDeepEquals(CheckstyleMetric.supportedMetrics(),
			new NativeMetric("File length", CLASS, JAVA),
			new NativeMetric("Number of methods", CLASS, JAVA));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkNativeMetricNames() {
		assertEquals("File length", CheckstyleMetric.getNativeMetricFor("maxLen.file").getName());
		assertEquals("Number of methods", CheckstyleMetric.getNativeMetricFor("too.many.methods").getName());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkNativeMetricForKeys() {
		assertDeepEquals(FILE_LENGTH.getNativeMetric(), CheckstyleMetric.getNativeMetricFor("maxLen.file"));
		assertDeepEquals(NUMBER_OF_METHODS.getNativeMetric(), CheckstyleMetric.getNativeMetricFor("too.many.methods"));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkNames() {
		assertEquals("File length", "" + FILE_LENGTH);
		assertEquals("Number of methods", "" + NUMBER_OF_METHODS);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldAddAttributesToConfiguration() {
		assertEquals("-1", checker.getChildByName("FileLength").getAttribute("max"));
		assertEquals("-1", treeWalker.getChildByName("MethodCount").getAttribute("maxTotal"));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldAddMessagesToConfiguration() {
		assertTrue(checker.getChildByName("FileLength").getMessages().containsKey("maxLen.file"));
		assertTrue(treeWalker.getChildByName("MethodCount").getMessages().containsKey("too.many.methods"));
	}
}