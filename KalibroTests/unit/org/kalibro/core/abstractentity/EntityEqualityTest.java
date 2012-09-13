package org.kalibro.core.abstractentity;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.TestCase;

public class EntityEqualityTest extends TestCase {

	private Person carlos;
	private EntityEquality equality;

	@Before
	public void setUp() {
		carlos = loadFixture("person-carlos", Person.class);
		equality = new EntityEquality();
	}

	@Test
	public void shouldEvaluateAnyTypeOfEntity() {
		assertTrue(equality.canEvaluate(new Person()));
		assertTrue(equality.canEvaluate(new Programmer()));
		assertTrue(equality.canEvaluate(new NoIdentityEntity()));

		assertFalse(equality.canEvaluate(null));
		assertFalse(equality.canEvaluate(this));
		assertFalse(equality.canEvaluate(equality));
	}

	@Test
	public void identityFieldsShouldBeEqual() {
		assertFalse(equalTo(new NoIdentityEntity()));
	}

	@Test
	public void identityValuesShouldBeEqual() {
		assertFalse(equalTo(new Person()));
		assertFalse(equalTo(new Person("0", "Carlos Morais", "Male")));
		assertFalse(equalTo(new Person("X", "Carlos Morais", "Male")));

		assertTrue(equalTo(new Person("CM", "", "")));
		assertTrue(equalTo(new Person("CM", "Cristina", "Female")));
	}

	@Test
	public void dontNeedToBeOfSameClass() {
		assertTrue(equalTo(loadFixture("programmer-carlos", Programmer.class)));
		assertTrue(equalTo(new PersonImitation(carlos)));
	}

	private boolean equalTo(AbstractEntity<?> other) {
		return equality.equals(carlos, other);
	}

	private class PersonImitation extends AbstractEntity<PersonImitation> {

		@IdentityField
		@SuppressWarnings("unused")
		private String identityNumber;

		protected PersonImitation(Person person) {
			this.identityNumber = person.getIdentityNumber();
		}
	}
}