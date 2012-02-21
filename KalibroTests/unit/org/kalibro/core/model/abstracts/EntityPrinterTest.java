package org.kalibro.core.model.abstracts;

import static org.junit.Assert.*;
import static org.kalibro.core.model.abstracts.PersonFixtures.*;
import static org.kalibro.core.model.abstracts.ProgrammerFixtures.*;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;

public class EntityPrinterTest extends KalibroTestCase {

	private EntityPrinter personPrinter, programmerPrinter;

	@Before
	public void setUp() {
		personPrinter = new EntityPrinter(carlos());
		programmerPrinter = new EntityPrinter(programmerCarlos());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldPrintIdentityFields() {
		assertEquals("$Person(identityNumber = CM)", personPrinter.simplePrint());
		assertEquals("$Programmer(identityNumber = CM)", programmerPrinter.simplePrint());
		String expected = "$PersonWrapper(person = $Person(identityNumber = CM))";
		assertEquals(expected, new EntityPrinter(new PersonWrapper()).simplePrint());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldPrintNull() {
		PersonWrapper entity = new PersonWrapper();
		entity.person = null;
		assertEquals("$PersonWrapper(person = null)", new EntityPrinter(entity).simplePrint());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldPrintAllFieldsOnDeepPrint() {
		String expected = "$Person(identityNumber = CM, name = Carlos Morais, relatives = {" +
			"mother = $Person(identityNumber = CN, name = Cristina Nascimento, relatives = {}, sex = Female), " +
			"sister = $Person(identityNumber = IN, name = Isis Nascimento, relatives = {}, sex = Female)" +
			"}, sex = Male)";
		assertEquals(expected, personPrinter.deepPrint());

		expected = "$Programmer(colleagues = {$Programmer(colleagues = {}, identityNumber = PM, " +
			"name = Paulo Meirelles, relatives = {}, sex = Male, useMetrics = true)}, " +
			"identityNumber = CM, name = Carlos Morais, relatives = {}, sex = Male, useMetrics = true)";
		assertEquals(expected, programmerPrinter.deepPrint());
	}

	private class PersonWrapper extends AbstractEntity<PersonWrapper> {

		@IdentityField
		@SuppressWarnings("unused" /* read via reflection */)
		private Person person = carlos();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldPrintAllFieldsWhenThereIsNoIdentity() {
		String expected = "$NoIdentityEntity(field1 = null, field2 = null)";
		assertEquals(expected, new EntityPrinter(new NoIdentityEntity()).simplePrint());
	}
}