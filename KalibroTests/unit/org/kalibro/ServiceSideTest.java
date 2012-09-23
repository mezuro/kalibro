package org.kalibro;

import org.kalibro.tests.EnumerationTest;

public class ServiceSideTest extends EnumerationTest<ServiceSide> {

	@Override
	protected Class<ServiceSide> enumerationClass() {
		return ServiceSide.class;
	}

	@Override
	protected String expectedText(ServiceSide value) {
		return value.name();
	}
}