package org.checkstyle;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;

import com.puppycrawl.tools.checkstyle.api.CheckstyleException;
import com.puppycrawl.tools.checkstyle.api.Configuration;

public class CheckstyleConfigurationTest extends KalibroTestCase {

	private CheckstyleConfiguration configuration;

	@Before
	public void setUp() {
		configuration = new CheckstyleConfiguration();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkName() {
		assertEquals("Checker", configuration.getName());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkAttributeNames() {
		List<String> attributeNames = Arrays.asList(configuration.getAttributeNames());
		assertDeepEquals(attributeNames, "localeCountry", "localeLanguage");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkAttributes() throws CheckstyleException {
		assertEquals("", configuration.getAttribute("localeCountry"));
		assertEquals("", configuration.getAttribute("localeLanguage"));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkMessages() {
		assertTrue(configuration.getMessages().isEmpty());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkChildren() {
		assertDeepEquals(getChildrenNames(configuration), "FileLength", "TreeWalker");

		Configuration treeWalker = getChildByName(configuration, "TreeWalker");
		assertDeepEquals(getChildrenNames(treeWalker), "MethodCount");
	}

	private Configuration getChildByName(CheckstyleConfiguration parent, String childName) {
		for (Configuration child : parent.getChildren())
			if (child.getName().equals(childName))
				return child;
		return null;
	}

	private Collection<String> getChildrenNames(Configuration parent) {
		List<String> names = new ArrayList<String>();
		for (Configuration child : parent.getChildren())
			names.add(child.getName());
		return names;
	}
}