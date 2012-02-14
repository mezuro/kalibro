package org.kalibro.core.util;

import static org.junit.Assert.*;

import org.junit.Test;
import org.kalibro.KalibroTestCase;

public class IdentifierTest extends KalibroTestCase {

	private static final String CLASS_NAME = "MyIdentifier4Testing";
	private static final String CONSTANT = "MY_IDENTIFIER_4_TESTING";
	private static final String VARIABLE = "myIdentifier4Testing";
	private static final String TEXT = "My identifier 4 testing";

	@Test(timeout = UNIT_TIMEOUT)
	public void testFromConstant() {
		assertEquals(CLASS_NAME, Identifier.fromConstant(CONSTANT).asClassName());
		assertEquals(CONSTANT, Identifier.fromConstant(CONSTANT).asConstant());
		assertEquals(TEXT, Identifier.fromConstant(CONSTANT).asText());
		assertEquals(VARIABLE, Identifier.fromConstant(CONSTANT).asVariable());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testFromText() {
		assertEquals(CLASS_NAME, Identifier.fromText(TEXT).asClassName());
		assertEquals(CONSTANT, Identifier.fromText(TEXT).asConstant());
		assertEquals(TEXT, Identifier.fromText(TEXT).asText());
		assertEquals(VARIABLE, Identifier.fromText(TEXT).asVariable());

		String complexText = "My identifier (4 - testing)";
		assertEquals(CLASS_NAME, Identifier.fromText(complexText).asClassName());
		assertEquals(CONSTANT, Identifier.fromText(complexText).asConstant());
		assertEquals(TEXT, Identifier.fromText(complexText).asText());
		assertEquals(VARIABLE, Identifier.fromText(complexText).asVariable());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testFromVariable() {
		assertEquals(CLASS_NAME, Identifier.fromVariable(VARIABLE).asClassName());
		assertEquals(CONSTANT, Identifier.fromVariable(VARIABLE).asConstant());
		assertEquals(TEXT, Identifier.fromVariable(VARIABLE).asText());
		assertEquals(VARIABLE, Identifier.fromVariable(VARIABLE).asVariable());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testNumbers() {
		assertEquals("MY_42", Identifier.fromConstant("MY_42").asConstant());
		assertEquals("MY_42", Identifier.fromVariable("my42").asConstant());
	}
}