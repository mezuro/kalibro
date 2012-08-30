package org.kalibro;

public class ServiceSideTest extends EnumerationTestCase<ServiceSide> {

	@Override
	protected Class<ServiceSide> enumerationClass() {
		return ServiceSide.class;
	}

	@Override
	protected String expectedText(ServiceSide value) {
		return value.name();
	}
}