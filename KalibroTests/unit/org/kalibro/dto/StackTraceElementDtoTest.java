package org.kalibro.dto;

public class StackTraceElementDtoTest extends AbstractDtoTest<StackTraceElement> {

	@Override
	protected StackTraceElement loadFixture() {
		return new ThrowableDtoTest().loadFixture().getStackTrace()[0];
	}
}