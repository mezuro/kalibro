package org.kalibro.core.model.abstracts;

import static org.junit.Assert.*;

import org.junit.Test;
import org.kalibro.KalibroTestCase;

public class HashCodeCalculatorTest extends KalibroTestCase {

	@Test(timeout = UNIT_TIMEOUT)
	public void checkHashCodeForOneField() {
		Person person = PersonFixtures.carlos();
		Integer codeSum = 1 + person.getIdentityNumber().hashCode();
		assertEquals(codeSum.hashCode(), new HashCodeCalculator(person).calculate());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkHashCodeForTwoFields() {
		NoIdentityEntity entity = new NoIdentityEntity();
		entity.field1 = "1";
		entity.field2 = "2";
		Integer codeSum = 1 + "1".hashCode() + "2".hashCode();
		assertEquals(codeSum.hashCode(), new HashCodeCalculator(entity).calculate());
	}
}