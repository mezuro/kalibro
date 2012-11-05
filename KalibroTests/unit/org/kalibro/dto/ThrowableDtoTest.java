package org.kalibro.dto;

import java.lang.reflect.InvocationTargetException;

public class ThrowableDtoTest extends AbstractDtoTest<Throwable> {

	@Override
	public void setUp() throws Exception {
		super.setUp();
		doReturn(entity.toString()).when(dto, "targetString");
		doReturn(entity.getMessage()).when(dto, "message");
		doReturn(entity.getCause()).when(dto, "cause");
		doReturn(entity.getStackTrace()).when(dto, "stackTrace");
	}

	@Override
	protected Throwable loadFixture() {
		return new Throwable("target", new InvocationTargetException(new NullPointerException(), "cause"));
	}
}