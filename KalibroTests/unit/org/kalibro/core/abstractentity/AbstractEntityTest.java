package org.kalibro.core.abstractentity;

import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroTestCase;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({AbstractEntity.class, Equality.class, HashCodeCalculator.class, Printer.class})
public class AbstractEntityTest extends KalibroTestCase {

	private Person entity;

	@Before
	public void setUp() {
		entity = new Person();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldPrintWithPrinter() {
		mockStatic(Printer.class);
		when(Printer.print(entity)).thenReturn("42");
		assertEquals("42", entity.toString());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldUseHashCodeCalculator() {
		mockStatic(HashCodeCalculator.class);
		when(HashCodeCalculator.hash(entity)).thenReturn(42);
		assertEquals(42, entity.hashCode());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldUseEqualityOnEquals() {
		spy(Equality.class);
		assertFalse(entity.equals(null));
		verifyStatic();
		Equality.areEqual(entity, null);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldUseEqualityOnDeepEquals() {
		spy(Equality.class);
		assertFalse(entity.deepEquals(null));
		verifyStatic();
		Equality.areDeepEqual(entity, null);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldUseEntityComparatorOnComparisons() throws Exception {
		EntityComparator<Person> comparator = mock(EntityComparator.class);
		whenNew(EntityComparator.class).withNoArguments().thenReturn(comparator);
		when(comparator.compare(entity, entity)).thenReturn(42);
		assertEquals(42, entity.compareTo(entity));
	}
}