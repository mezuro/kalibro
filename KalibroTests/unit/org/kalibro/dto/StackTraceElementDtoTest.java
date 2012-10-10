package org.kalibro.dto;

public class StackTraceElementDtoTest extends AbstractDtoTest<StackTraceElement> {

	@Override
	protected StackTraceElement loadFixture() {
		return new Exception().getStackTrace()[0];
	}
}