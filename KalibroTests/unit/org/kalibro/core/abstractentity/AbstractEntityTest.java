package org.kalibro.core.abstractentity;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.tests.UnitTest;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(
		{ AbstractEntity.class,
			Equality.class,
			FileUtils.class,
			HashCodeCalculator.class,
			Printer.class })

public class AbstractEntityTest extends UnitTest {

	private File file;
	private Person entity;

	@Before
	public void setUp() {
		file = mock(File.class);
		entity = loadFixture("carlos", Person.class);
		mockStatic(FileUtils.class);
	}

	@Test
	public void shouldImportFromFile() throws Exception {
		assertDeepEquals(entity, AbstractEntity.importFrom(
				getFile("Person-carlos.yml"), Person.class));
	}

	@Test
	public void shouldThrowExceptionWhenCannotImport() throws Exception {
		whenNew(FileInputStream.class).withArguments(file).thenThrow(new NullPointerException());
		assertThat(new VoidTask() {

			@Override
			protected void perform() {
				AbstractEntity.importFrom(file, Person.class);
			}
		}).throwsException().withMessage("Could not import person from file: " + file)
			.withCause(NullPointerException.class);
	}

	@Test
	public void shouldExportToFile() throws Exception {
		entity.exportTo(file);
		verifyStatic();
		FileUtils.writeStringToFile(file, Printer.print(entity));
	}

	@Test
	public void shouldThrowExceptionWhenCannotExport() throws Exception {
		doThrow(new IOException()).when(FileUtils.class);
		FileUtils.writeStringToFile(file, Printer.print(entity));
		assertThat(new VoidTask() {

			@Override
			protected void perform() {
				entity.exportTo(file);
			}
		}).throwsException().withMessage(
				"Could not export person to file: "
				+ file).withCause(IOException.class);
	}

	@Test
	public void shouldPrintWithPrinter() {
		mockStatic(Printer.class);
		when(Printer.print(entity)).thenReturn("42");
		assertEquals("42", entity.toString());
	}

	@Test
	public void shouldUseHashCodeCalculator() {
		mockStatic(HashCodeCalculator.class);
		when(HashCodeCalculator.hash(entity)).thenReturn(42);
		assertEquals(42, entity.hashCode());
	}

	@Test
	public void shouldUseEqualityOnEquals() {
		spy(Equality.class);
		assertFalse(entity.equals(null));
		verifyStatic();
		Equality.areEqual(entity, null);
	}

	@Test
	public void shouldUseEqualityOnDeepEquals() {
		spy(Equality.class);
		assertFalse(entity.deepEquals(null));
		verifyStatic();
		Equality.areDeepEqual(entity, null);
	}

	@Test
	public void shouldUseEntityComparatorOnComparisons() throws Exception {
		EntityComparator<Person> comparator = mock(EntityComparator.class);
		whenNew(EntityComparator.class).withNoArguments().thenReturn(comparator);
		when(comparator.compare(entity, entity)).thenReturn(42);
		assertEquals(42, entity.compareTo(entity));
	}

	@Test
	public void shouldThrowExceptionForCondition() {
		final String message = "AbstractEntityTest message";
		entity.throwExceptionIf(false, message);
		assertThat(new VoidTask() {

			@Override
			protected void perform() throws Throwable {
				entity.throwExceptionIf(true, message);
			}
		}).throwsException().withMessage(message);
	}
}
