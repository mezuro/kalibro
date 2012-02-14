package org.kalibro.desktop.swingextension.field;

import java.text.DecimalFormat;

public class DoubleField extends NumberField<Double> {

	public DoubleField(String name) {
		super(name);
	}

	public DoubleField(String name, Double specialNumber) {
		super(name, specialNumber);
	}

	@Override
	public DecimalFormat getDecimalFormat() {
		DecimalFormat format = new DecimalFormat();
		format.setMinimumFractionDigits(2);
		format.setMaximumFractionDigits(2);
		return format;
	}

	@Override
	public Double parseValue(Number value) {
		return value.doubleValue();
	}

	@Override
	protected int getColumns() {
		return 8;
	}
}