package org.kalibro.core.abstractentity;

import static org.junit.Assert.*;
import static org.kalibro.core.abstractentity.PersonFixtures.carlos;
import static org.kalibro.core.abstractentity.ProgrammerFixtures.programmerCarlos;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;

public class DeepEntityEqualityEvaluatorTest extends KalibroTestCase {

	private Person person;
	private Programmer programmer;

	@Before
	public void setUp() {
		person = carlos();
		programmer = programmerCarlos();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldAcceptSame() {
		assertDeepEquals(true, person, person);
		assertDeepEquals(true, programmer, programmer);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldAcceptEqual() {
		assertDeepEquals(true, person, carlos());
		assertDeepEquals(true, programmer, programmerCarlos());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNeitherAcceptSubclassOrSuperclass() {
		assertDeepEquals(false, person, programmer);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotAcceptNull() {
		assertFalse(new DeepEntityEqualityEvaluator(person, null).areEqual());
		assertFalse(new DeepEntityEqualityEvaluator(programmer, null).areEqual());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotAcceptNullField() {
		person.setName(null);
		assertDeepEquals(false, person, carlos());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldAcceptCorrespondingNullFields() {
		person.setName(null);
		Person equals = PersonFixtures.carlos();
		equals.setName(null);
		assertDeepEquals(true, person, equals);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotAcceptDifferentFieldValues() {
		Person almostEquals = PersonFixtures.carlos();
		almostEquals.setName("Carlos Morais de Oliveira");
		assertDeepEquals(false, person, almostEquals);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldAcceptEquivalentExceptions() {
		assertDeepEquals(true, new ExceptionEntity(), new ExceptionEntity());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldAcceptNotAcceptExceptionWithDifferentStackTraces() {
		ExceptionEntity entity1 = new ExceptionEntity();
		ExceptionEntity entity2 = new ExceptionEntity();
		assertDeepEquals(false, entity1, entity2);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotAcceptDifferentMapSizes() {
		Person almostEquals = PersonFixtures.carlos();
		almostEquals.getRelatives().remove("sister");
		assertDeepEquals(false, person, almostEquals);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotAcceptDifferentMapElements() {
		Person almostEquals = PersonFixtures.carlos();
		almostEquals.getRelatives().remove("sister");
		almostEquals.getRelatives().put("sister", new Person());
		assertDeepEquals(false, person, almostEquals);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotAcceptDifferentCollectionSizes() {
		Programmer almostEquals = programmerCarlos();
		almostEquals.getColleagues().remove(0);
		assertDeepEquals(false, programmer, almostEquals);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotAcceptDifferentCollectionElements() {
		Programmer almostEquals = programmerCarlos();
		almostEquals.getColleagues().remove(0);
		almostEquals.getColleagues().add(new Programmer(new Person(), false));
		assertDeepEquals(false, programmer, almostEquals);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotAcceptDifferentCollectionElementFieldValues() {
		Programmer almostEquals = programmerCarlos();
		almostEquals.getColleagues().get(0).setUseMetrics(false);
		assertDeepEquals(false, programmer, almostEquals);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotAcceptDifferentMapInsideMapSizes() {
		Person almostEquals = PersonFixtures.carlos();
		almostEquals.getRelatives().get("sister").addRelative("mother", PersonFixtures.cristina());
		assertDeepEquals(false, programmer, almostEquals);
	}

	private void assertDeepEquals(boolean deepEquals, AbstractEntity<?> entity1, AbstractEntity<?> entity2) {
		assertEquals(deepEquals, new DeepEntityEqualityEvaluator(entity1, entity2).areEqual());
		assertEquals(deepEquals, new DeepEntityEqualityEvaluator(entity2, entity1).areEqual());
	}
}