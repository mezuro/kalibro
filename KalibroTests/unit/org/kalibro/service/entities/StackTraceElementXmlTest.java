package org.kalibro.service.entities;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;

import org.kalibro.DtoTestCase;

public class StackTraceElementXmlTest extends DtoTestCase<StackTraceElement, StackTraceElementXml> {

	@Override
	protected StackTraceElementXml newDtoUsingDefaultConstructor() {
		return new StackTraceElementXml();
	}

	@Override
	protected Collection<StackTraceElement> entitiesForTestingConversion() {
		return Arrays.asList(new Exception().getStackTrace());
	}

	@Override
	protected StackTraceElementXml createDto(StackTraceElement element) {
		return new StackTraceElementXml(element);
	}

	@Override
	protected void assertCorrectConversion(StackTraceElement original, StackTraceElement converted) {
		assertEquals(original.getClassName(), converted.getClassName());
		assertEquals(original.getMethodName(), converted.getMethodName());
		assertEquals(original.getFileName(), converted.getFileName());
		assertEquals(original.getLineNumber(), converted.getLineNumber());
	}
}