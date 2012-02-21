package org.kalibro.core.persistence.database.entities;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.kalibro.DtoTestCase;

public class StackTraceElementRecordTest extends DtoTestCase<StackTraceElement, StackTraceElementRecord> {

	@Override
	protected StackTraceElementRecord newDtoUsingDefaultConstructor() {
		return new StackTraceElementRecord();
	}

	@Override
	protected Collection<StackTraceElement> entitiesForTestingConversion() {
		return Arrays.asList(new Exception().getStackTrace());
	}

	@Override
	protected StackTraceElementRecord createDto(StackTraceElement element) {
		return new StackTraceElementRecord(element, null, null);
	}

	@Override
	protected void assertCorrectConversion(StackTraceElement original, StackTraceElement converted) {
		assertEquals(original.getClassName(), converted.getClassName());
		assertEquals(original.getMethodName(), converted.getMethodName());
		assertEquals(original.getFileName(), converted.getFileName());
		assertEquals(original.getLineNumber(), converted.getLineNumber());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldPutItselfConvertedOnStackTrace() {
		StackTraceElement element = new Exception().getStackTrace()[7];
		StackTraceElementRecord record = new StackTraceElementRecord(element, null, 7);

		StackTraceElement[] stackTrace = new StackTraceElement[10];
		record.putIntoStackTrace(stackTrace);
		assertCorrectConversion(element, stackTrace[7]);
	}
}