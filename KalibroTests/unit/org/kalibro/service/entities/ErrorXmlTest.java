package org.kalibro.service.entities;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;

import org.kalibro.DtoTestCase;

public class ErrorXmlTest extends DtoTestCase<Exception, ErrorXml> {

	@Override
	protected ErrorXml newDtoUsingDefaultConstructor() {
		return new ErrorXml();
	}

	@Override
	protected Collection<Exception> entitiesForTestingConversion() {
		return Arrays.asList(new Exception("My error message"));
	}

	@Override
	protected ErrorXml createDto(Exception error) {
		return new ErrorXml(error);
	}

	@Override
	protected void assertCorrectConversion(Exception original, Exception converted) {
		assertEquals(original.getMessage(), converted.getMessage());
		assertCorrectConversion(original.getStackTrace(), converted.getStackTrace());
	}

	private void assertCorrectConversion(StackTraceElement[] original, StackTraceElement[] converted) {
		assertEquals(original.length, converted.length);
		StackTraceElementXmlTest test = new StackTraceElementXmlTest();
		for (int i = 0; i < original.length; i++)
			test.assertCorrectConversion(original[i], converted[i]);
	}
}