package org.kalibro.core.model.abstracts;

import static org.junit.Assert.*;
import static org.kalibro.core.model.abstracts.PersonFixtures.*;
import static org.kalibro.core.model.abstracts.ProgrammerFixtures.*;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;

public class DeepEqualityEvaluatorTest extends KalibroTestCase {

	private Person person;
	private Programmer programmer;

	@Before
	public void setUp() {
		person = carlos();
		programmer = programmerCarlos();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldAcceptSame() {
		checkDeepEquals(person, person);
		checkDeepEquals(programmer, programmer);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldAcceptEqual() {
		checkDeepEquals(person, carlos());
		checkDeepEquals(programmer, programmerCarlos());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNeitherAcceptSubclassOrSuperclass() {
		checkNotDeepEquals(person, programmer);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotAcceptNull() {
		assertFalse(new DeepEqualityEvaluator(person).isEquals(null));
		assertFalse(new DeepEqualityEvaluator(programmer).isEquals(null));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotAcceptNullField() {
		person.setName(null);
		checkNotDeepEquals(person, carlos());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldAcceptCorrespondingNullFields() {
		person.setName(null);
		Person equals = PersonFixtures.carlos();
		equals.setName(null);
		assertTrue(new DeepEqualityEvaluator(person).isEquals(equals));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotAcceptDifferentFieldValues() {
		Person almostEquals = PersonFixtures.carlos();
		almostEquals.setName("Carlos Morais de Oliveira");
		checkNotDeepEquals(person, almostEquals);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotAcceptDifferentMapSizes() {
		Person almostEquals = PersonFixtures.carlos();
		almostEquals.getRelatives().remove("sister");
		checkNotDeepEquals(person, almostEquals);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotAcceptDifferentMapElements() {
		Person almostEquals = PersonFixtures.carlos();
		almostEquals.getRelatives().remove("sister");
		almostEquals.getRelatives().put("sister", new Person());
		checkNotDeepEquals(person, almostEquals);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotAcceptDifferentCollectionSizes() {
		Programmer almostEquals = programmerCarlos();
		almostEquals.getColleagues().remove(0);
		checkNotDeepEquals(programmer, almostEquals);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotAcceptDifferentCollectionElements() {
		Programmer almostEquals = programmerCarlos();
		almostEquals.getColleagues().remove(0);
		almostEquals.getColleagues().add(new Programmer(new Person(), false));
		checkNotDeepEquals(programmer, almostEquals);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotAcceptDifferentCollectionElementFieldValues() {
		Programmer almostEquals = programmerCarlos();
		almostEquals.getColleagues().get(0).setUseMetrics(false);
		checkNotDeepEquals(programmer, almostEquals);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotAcceptDifferentMapInsideMapSizes() {
		Person almostEquals = PersonFixtures.carlos();
		almostEquals.getRelatives().get("sister").addRelative("mother", PersonFixtures.cristina());
		checkNotDeepEquals(programmer, almostEquals);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldAcceptEqualArrays() {
		checkDeepEquals(new ArrayHolder(), new ArrayHolder());
	}

	private void checkDeepEquals(AbstractEntity<?> entity1, AbstractEntity<?> entity2) {
		assertTrue(new DeepEqualityEvaluator(entity1).isEquals(entity2));
		assertTrue(new DeepEqualityEvaluator(entity2).isEquals(entity1));
	}

	private void checkNotDeepEquals(AbstractEntity<?> entity1, AbstractEntity<?> entity2) {
		assertFalse(new DeepEqualityEvaluator(entity1).isEquals(entity2));
		assertFalse(new DeepEqualityEvaluator(entity2).isEquals(entity1));
	}

	private class ArrayHolder extends AbstractEntity<ArrayHolder> {

		@SuppressWarnings("unused")
		private String[] array = {"My", "string", "array"};
	}
}