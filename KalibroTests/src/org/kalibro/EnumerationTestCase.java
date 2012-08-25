package org.kalibro;

import static org.junit.Assert.*;

import org.junit.Test;
import org.kalibro.core.util.Identifier;

public abstract class EnumerationTestCase<ENUM extends Enum<ENUM>> extends TestCase {

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldConvertFromString() throws Exception {
		for (ENUM value : values())
			assertSame(value, valueOf(value.name()));
	}

	private ENUM valueOf(String name) throws Exception {
		return (ENUM) enumerationClass().getMethod("valueOf", String.class).invoke(null, name);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldPrintAsText() throws Exception {
		for (ENUM value : values())
			assertEquals(expectedText(value), "" + value);
	}

	private ENUM[] values() throws Exception {
		return (ENUM[]) enumerationClass().getMethod("values").invoke(null);
	}

	protected String expectedText(ENUM value) {
		return Identifier.fromConstant(value.name()).asText();
	}

	protected abstract Class<ENUM> enumerationClass();
}