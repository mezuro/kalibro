package org.kalibro.service.entities;

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
}