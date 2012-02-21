package org.kalibro.desktop.swingextension.field;

import java.text.DecimalFormat;

class ByteField extends NumberField<Byte> {

	public ByteField(String name) {
		super(name);
	}

	public ByteField(String name, Byte specialNumber) {
		super(name, specialNumber);
	}

	@Override
	public DecimalFormat getDecimalFormat() {
		return new DecimalFormat();
	}

	@Override
	protected Byte parseValue(Number value) {
		return value.byteValue();
	}

	@Override
	protected int getColumns() {
		return 4;
	}
}