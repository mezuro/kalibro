package org.checkstyle;

import static org.checkstyle.CheckstyleMetric.*;
import static org.junit.Assert.*;
import static org.kalibro.core.model.enums.Granularity.*;
import static org.kalibro.core.model.enums.Language.*;

import com.puppycrawl.tools.checkstyle.DefaultConfiguration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.model.NativeMetric;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DefaultConfiguration.class)
public class CheckstyleMetricTest extends KalibroTestCase {

	@Test(timeout = UNIT_TIMEOUT)
	public void checkSupportedMetrics() {
		assertDeepEquals(CheckstyleMetric.supportedMetrics(),
			new NativeMetric("File length", CLASS, JAVA),
			new NativeMetric("Number of methods", CLASS, JAVA));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkNames() {
		assertEquals("File length", "" + FILE_LENGTH);
		assertEquals("Number of methods", "" + NUMBER_OF_METHODS);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkNativeMetrics() {
		assertDeepEquals(new NativeMetric("File length", CLASS, JAVA), FILE_LENGTH.getNativeMetric());
		assertDeepEquals(new NativeMetric("Number of methods", CLASS, JAVA), NUMBER_OF_METHODS.getNativeMetric());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkPatterns() {
		assertEquals("^File length is (\\d+) lines \\(max allowed is -1\\)\\.$",
			FILE_LENGTH.getPattern().pattern());
		assertEquals("^Total number of methods is (\\d+) \\(max allowed is -1\\)\\.$",
			NUMBER_OF_METHODS.getPattern().pattern());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldChangeParentToTreeWalkerOnNumberOfMethods() {
		assertSameParent(FILE_LENGTH);
		assertParentChange(NUMBER_OF_METHODS, "TreeWalker");

	}

	private void assertSameParent(CheckstyleMetric metric) {
		DefaultConfiguration parent = PowerMockito.mock(DefaultConfiguration.class);
		assertSame(parent, metric.addToConfiguration(parent));
	}

	private void assertParentChange(CheckstyleMetric metric, String newParent) {
		DefaultConfiguration parent = PowerMockito.mock(DefaultConfiguration.class);
		assertEquals(newParent, metric.addToConfiguration(parent).getName());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldAddConfigurationToParent() throws Exception {
		testConfiguration(FILE_LENGTH, "FileLength", "max", "-1");
		testConfiguration(NUMBER_OF_METHODS, "TreeWalker");
	}

	private void testConfiguration(CheckstyleMetric metric, String moduleName, String... properties) throws Exception {
		DefaultConfiguration parent = PowerMockito.mock(DefaultConfiguration.class);
		metric.addToConfiguration(parent);

		ArgumentCaptor<DefaultConfiguration> captor = ArgumentCaptor.forClass(DefaultConfiguration.class);
		Mockito.verify(parent).addChild(captor.capture());
		DefaultConfiguration argument = captor.getValue();
		assertEquals(moduleName, argument.getName());
		for (int i = 0; i < properties.length; i += 2)
			assertEquals(properties[i + 1], argument.getAttribute(properties[i]));
	}
}