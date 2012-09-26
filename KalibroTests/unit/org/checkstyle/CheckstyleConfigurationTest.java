package org.checkstyle;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.NativeMetric;
import org.kalibro.tests.UnitTest;

public class CheckstyleConfigurationTest extends UnitTest {

	private CheckstyleConfiguration configuration;

	@Before
	public void setUp() {
		configuration = new CheckstyleConfiguration("The module name");
	}

	@Test
	public void checkName() {
		assertEquals("The module name", configuration.getName());
	}

	@Test
	public void checkAttributes() {
		assertArrayEquals(new String[0], configuration.getAttributeNames());

		configuration.addAttributeName("my attribute");
		assertArrayEquals(new String[]{"my attribute"}, configuration.getAttributeNames());
	}

	@Test
	public void attributeShouldBeAlwaysBeOneNegative() {
		assertEquals("-1", configuration.getAttribute("anything"));
	}

	@Test
	public void checkMessages() {
		assertTrue(configuration.getMessages().isEmpty());

		String key = "my message key";
		configuration.addMessageKey(key);
		assertTrue(configuration.getMessages().containsKey(key));
		assertEquals(key + "{0}", configuration.getMessages().get(key));
	}

	@Test
	public void shouldCreateChildOnFirstGetByName() {
		assertEquals(0, configuration.getChildren().length);

		CheckstyleConfiguration fileLength = configuration.getChildByName("FileLength");
		assertEquals(1, configuration.getChildren().length);
		assertSame(fileLength, configuration.getChildByName("FileLength"));
	}

	@Test
	public void shouldCreateCheckerConfigurationFilteringMetrics() {
		NativeMetric numberOfMethods = CheckstyleMetric.NUMBER_OF_METHODS.getNativeMetric();
		configuration = CheckstyleConfiguration.checkerConfiguration(asList(numberOfMethods));

		assertEquals(1, configuration.getChildren().length);

		CheckstyleConfiguration treeWalker = configuration.getChildByName("TreeWalker");
		assertEquals(1, treeWalker.getChildren().length);

		CheckstyleConfiguration methodCount = treeWalker.getChildByName("MethodCount");
		assertTrue(methodCount.getMessages().containsKey("too.many.methods"));
	}
}