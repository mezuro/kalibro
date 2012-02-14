package org.kalibro.spago;

import static org.junit.Assert.*;
import static org.kalibro.core.model.enums.RepositoryType.*;

import java.io.PrintStream;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.command.StringOutputStream;
import org.kalibro.core.concurrent.Task;
import org.kalibro.core.model.Project;
import org.kalibro.core.model.Repository;
import org.xml.sax.SAXParseException;

public class SpagoRequestParserTest extends KalibroTestCase {

	private SpagoRequestParser parser;

	@Test(timeout = UNIT_TIMEOUT)
	public void checkParsedProject() throws Exception {
		Project expected = new Project();
		expected.setName("HelloWorld-1.0R1");
		expected.setLicense("Creative Commons");
		expected.setRepository(new Repository(SUBVERSION, "svn://my.repository/", "myUsername", "myPassword"));

		parseFile("HelloWorld.xml");
		assertDeepEquals(expected, parser.getProject());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testInvalidRequest() {
		checkException(new Task() {

			@Override
			public void perform() throws Exception {
				parseFile("HelloWorld_invalid.xml");
			}
		}, NullPointerException.class);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testBrokenRequest() {
		System.setErr(new PrintStream(new StringOutputStream()));
		checkException(new Task() {

			@Override
			public void perform() throws Exception {
				parseFile("HelloWorld_broken.xml");
			}
		}, SAXParseException.class);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testShouldInclude() throws Exception {
		testShouldInclude("HelloWorld.xml", true);
		testShouldInclude("HelloWorld_MOSST.xml", true);
		testShouldInclude("HelloWorld_C++.xml", true);

		testShouldInclude("HelloWorld_Java.xml", false);
		testShouldInclude("HelloWorld_no_model.xml", false);
	}

	private void testShouldInclude(String fileName, boolean shouldInclude) throws Exception {
		parseFile(fileName);
		assertEquals(shouldInclude, parser.shouldIncludeProject());
	}

	private void parseFile(String fileName) throws Exception {
		String xmlRequest = IOUtils.toString(getClass().getResourceAsStream(fileName));
		parser = new SpagoRequestParser(xmlRequest);
	}
}