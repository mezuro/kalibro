package org.checkstyle;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.model.NativeMetric;

public class CheckstyleConfigurationTest extends KalibroTestCase {

	private CheckstyleConfiguration configuration;

	@Before
	public void setUp() {
		configuration = new CheckstyleConfiguration("The module name");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkName() {
		assertEquals("The module name", configuration.getName());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkAttributes() {
		assertArrayEquals(new String[0], configuration.getAttributeNames());

		configuration.addAttributeName("my attribute");
		assertArrayEquals(new String[]{"my attribute"}, configuration.getAttributeNames());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void attributeShouldBeAlwaysBeOneNegative() {
		assertEquals("-1", configuration.getAttribute("anything"));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkMessages() {
		assertTrue(configuration.getMessages().isEmpty());

		String key = "my message key";
		configuration.addMessageKey(key);
		assertTrue(configuration.getMessages().containsKey(key));
		assertEquals(key + "{0}", configuration.getMessages().get(key));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldCreateChildOnFirstGetByName() {
		assertEquals(0, configuration.getChildren().length);

		CheckstyleConfiguration fileLength = configuration.getChildByName("FileLength");
		assertSame(fileLength, configuration.getChildByName("FileLength"));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldCreateCheckerConfigurationFilteringMetrics() {
		NativeMetric numberOfMethods = CheckstyleMetric.NUMBER_OF_METHODS.getNativeMetric();
		configuration = CheckstyleConfiguration.checkerConfiguration(Arrays.asList(numberOfMethods));

		assertEquals(1, configuration.getChildren().length);

		CheckstyleConfiguration treeWalker = configuration.getChildByName("TreeWalker");
		assertEquals(1, treeWalker.getChildren().length);

		CheckstyleConfiguration methodCount = treeWalker.getChildByName("MethodCount");
		assertTrue(methodCount.getMessages().containsKey("too.many.methods"));
	}
}