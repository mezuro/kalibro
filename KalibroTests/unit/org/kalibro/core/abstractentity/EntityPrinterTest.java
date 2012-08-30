package org.kalibro.core.abstractentity;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.introspector.BeanAccess;

public class EntityPrinterTest extends PrinterTestCase<AbstractEntity<?>> {

	private Programmer programmer;

	@Override
	protected Printer<AbstractEntity<?>> createPrinter() {
		return new EntityPrinter();
	}

	@Before
	public void setUp() {
		programmer = loadFixture("programmer-carlos", Programmer.class);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldPrintEntities() {
		assertTrue(printer.canPrint(new Person()));
		assertTrue(printer.canPrint(new Programmer()));

		assertFalse(printer.canPrint(this));
		assertFalse(printer.canPrint(printer));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldPrintAsYaml() throws Exception {
		assertEquals(loadResource("programmer-carlos.yml").replace("---", ""), print(programmer, ""));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldBeLoadableAsYaml() throws Exception {
		Yaml yaml = new Yaml();
		yaml.setBeanAccess(BeanAccess.FIELD);
		Programmer loaded = yaml.loadAs(print(programmer, ""), Programmer.class);
		assertDeepEquals(programmer, loaded);
	}
}