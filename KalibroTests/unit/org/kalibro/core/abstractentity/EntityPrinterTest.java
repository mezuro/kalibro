package org.kalibro.core.abstractentity;

import static org.junit.Assert.assertEquals;
import static org.kalibro.core.abstractentity.EntityPrinter.print;

import java.io.IOException;

import org.junit.Test;
import org.kalibro.KalibroTestCase;

public class EntityPrinterTest extends KalibroTestCase {

	private Person person;

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldPrintAsYaml() throws IOException {
		person = loadFixture("person-carlos", Person.class);
		assertEquals(loadResource("person-carlos.yml"), print(person));
	}
}