package org.kalibro.core;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.kalibro.tests.UnitTest;

public class IdentifierTest extends UnitTest {

	private static final String CLASS_NAME = "MyIdentifier44Testing";
	private static final String VARIABLE = "myIdentifier44Testing";
	private static final String TEXT = "My identifier 44 testing";
	private static final String CONSTANT = "MY_IDENTIFIER_44_TESTING";

	private Identifier identifier;

	@Test
	public void shouldCreateFromClassName() {
		identifier = Identifier.fromClassName(CLASS_NAME);
		shouldConvert();
	}

	@Test
	public void shouldCreateFromVariable() {
		identifier = Identifier.fromVariable(VARIABLE);
		shouldConvert();
	}

	@Test
	public void shouldCreateFromText() {
		identifier = Identifier.fromText(TEXT);
		shouldConvert();
	}

	@Test
	public void shouldCreateFromComplexText() {
		identifier = Identifier.fromText("_My identifier (44 - testing) àéü_");
		shouldConvert();
	}

	@Test
	public void shouldCreateFromConstant() {
		identifier = Identifier.fromConstant(CONSTANT);
		shouldConvert();
	}

	private void shouldConvert() {
		assertEquals(CLASS_NAME, identifier.asClassName());
		assertEquals(VARIABLE, identifier.asVariable());
		assertEquals(CONSTANT, identifier.asConstant());
		assertEquals(TEXT, identifier.asText());
	}
}