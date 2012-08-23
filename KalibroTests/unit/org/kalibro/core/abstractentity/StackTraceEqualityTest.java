package org.kalibro.core.abstractentity;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;

public class StackTraceEqualityTest extends KalibroTestCase {

	private StackTraceEquality equality;

	@Before
	public void setUp() {
		equality = new StackTraceEquality();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldEvaluateOnlyStackTraceElements() {
		assertTrue(equality.canEvaluate(newElement("Object.equals(Object.java:150)")));
		assertTrue(equality.canEvaluate(newElement("String.indexOf(String.java:1534)")));

		assertFalse(equality.canEvaluate(null));
		assertFalse(equality.canEvaluate(this));
		assertFalse(equality.canEvaluate(equality));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void declaringClassesShouldBeEqual() {
		assertFalse(equality.equals(newElement("Set.contains(Set.java:42)"), newElement("Map.contains(Set.java:42)")));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void methodNamesShouldBeEqual() {
		assertFalse(equality.equals(newElement("Set.contains(Set.java:42)"), newElement("Set.equals(Set.java:42)")));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void fileNamesShouldBeEqual() {
		assertFalse(equality.equals(newElement("Set.contains(Set.java:42)"), newElement("Set.contains(Map.java:42)")));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void lineNumbersShouldBeEqual() {
		assertFalse(equality.equals(newElement("Set.contains(Set.java:42)"), newElement("Set.contains(Set.java:84)")));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void allFourShouldBeEqual() {
		assertFalse(equality.equals(newElement("Set.contains(Set.java:42)"), newElement("Set.contains(Set.java:42)")));
	}

	private StackTraceElement newElement(String shortPrint) {
		String declaringClass = shortPrint.replaceAll("\\..*", "");
		String methodName = shortPrint.replaceAll("\\(.*\\)", "").replaceAll(".*\\.", "");
		String fileName = shortPrint.replaceAll(".*\\(", "").replaceAll("\\:.*", "");
		int lineNumber = Integer.parseInt(shortPrint.replaceAll(".*\\:", "").replace(")", ""));
		return new StackTraceElement(declaringClass, methodName, fileName, lineNumber);
	}
}