package org.checkstyle;

import static org.junit.Assert.*;

import com.puppycrawl.tools.checkstyle.api.Configuration;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.NativeMetric;
import org.kalibro.tests.UnitTest;

public class CheckstyleConfigurationTest extends UnitTest {

	private static final String NAME = "CheckstyleConfigurationTest name";
	private static final String ATTRIBUTE = "CheckstyleConfigurationTest attribute";
	private static final String MESSAGE_KEY = "CheckstyleConfigurationTest message key";

	private CheckstyleConfiguration configuration;

	@Before
	public void setUp() {
		configuration = new CheckstyleConfiguration(NAME);
	}

	@Test
	public void checkConstruction() {
		assertEquals(NAME, configuration.getName());
		assertTrue(configuration.getMessages().isEmpty());
		assertEquals(0, configuration.getChildren().length);
		assertEquals(0, configuration.getAttributeNames().length);
	}

	@Test
	public void shouldAddAttribute() {
		configuration.addAttribute(ATTRIBUTE);
		assertArrayEquals(array(ATTRIBUTE), configuration.getAttributeNames());
	}

	@Test
	public void attributeValueShouldAlwaysBeNegative() {
		assertEquals("-1", configuration.getAttribute(ATTRIBUTE));
	}

	@Test
	public void shouldAddMessageKey() {
		configuration.addMessageKey(MESSAGE_KEY);
		assertDeepEquals(map(MESSAGE_KEY, MESSAGE_KEY + "{0}"), configuration.getMessages());
	}

	@Test
	public void shouldCreateChildOnFirstGetByName() {
		CheckstyleConfiguration fileLength = configuration.getChildByName("FileLength");
		assertArrayEquals(array(fileLength), configuration.getChildren());
	}

	@Test
	public void shouldCreateCheckerConfigurationForWantedMetrics() {
		NativeMetric fanOut = loadFixture("fanOut", CheckstyleMetric.class);
		CheckstyleConfiguration checker = CheckstyleConfiguration.checkerConfiguration(set(fanOut));

		assertEquals(1, checker.getChildren().length);
		Configuration treeWalker = checker.getChildren()[0];

		assertEquals(1, treeWalker.getChildren().length);
		Configuration methodCount = treeWalker.getChildren()[0];

		assertArrayEquals(array("max"), methodCount.getAttributeNames());
		assertDeepEquals(map("classFanOutComplexity", "classFanOutComplexity{0}"), methodCount.getMessages());
	}
}