package org.kalibro.core.model.enums;

import static org.junit.Assert.*;
import static org.kalibro.core.model.enums.Language.*;

import org.junit.BeforeClass;
import org.junit.Test;
import org.kalibro.KalibroTestCase;

public class LanguageTest extends KalibroTestCase {

	@BeforeClass
	public static void emmaCoverage() {
		Language.values();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testToString() {
		assertEquals("C", "" + C);
		assertEquals("C++", "" + CPP);
		assertEquals("Java", "" + JAVA);
	}
}