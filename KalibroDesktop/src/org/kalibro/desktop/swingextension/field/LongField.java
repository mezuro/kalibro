package org.kalibro.desktop.swingextension.field;

import java.text.DecimalFormat;

public class LongField extends NumberField<Long> {

	public LongField(String name) {
		super(name);
	}

	public LongField(String name, Long specialNumber) {
		super(name, specialNumber);
	}

	@Override
	public DecimalFormat getDecimalFormat() {
		DecimalFormat format = new DecimalFormat();
		format.setMaximumFractionDigits(0);
		return format;
	}

	@Override
	public Long parseValue(Number value) {
		return value.longValue();
	}

	@Override
	protected int getColumns() {
		return 6;
	}
}