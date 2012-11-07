package org.kalibro.desktop.swingextension.field;

import java.text.DecimalFormat;

public class IntegerField extends NumberField<Integer> {

	public IntegerField(String name) {
		super(name);
	}

	@Override
	public DecimalFormat getDecimalFormat() {
		DecimalFormat format = new DecimalFormat();
		format.setMaximumFractionDigits(0);
		return format;
	}

	@Override
	protected Integer parseValue(Number value) {
		return value.intValue();
	}

	@Override
	protected int getColumns() {
		return 6;
	}
}